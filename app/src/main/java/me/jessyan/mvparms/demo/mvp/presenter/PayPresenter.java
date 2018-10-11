package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import javax.inject.Inject;

import cn.ehanmy.pay.PayManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.PayContract;
import me.jessyan.mvparms.demo.mvp.model.entity.pay.request.PayInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.pay.request.PayRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.pay.response.PayInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.pay.response.PayResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class PayPresenter extends BasePresenter<PayContract.Model, PayContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;

    private IWXAPI api;

    @Inject
    public PayPresenter(PayContract.Model model, PayContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        getOrderPayInfo();
    }


    private void getOrderPayInfo() {
        PayInfoRequest request = new PayInfoRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        String token = (String) cache.get(KEY_KEEP + "token");
        request.setToken(token);
        request.setOrderId(mRootView.getActivity().getIntent().getStringExtra("orderId"));
        mModel.getOrderPayInfo(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<PayInfoResponse>(mErrorHandler) {
                    @Override
                    public void onNext(PayInfoResponse response) {
                        if (response.isSuccess()) {
                            mRootView.updateUI(response);
                        }
                    }
                });
    }


    public void pay() {
        PayRequest request = new PayRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        String token = (String) cache.get(KEY_KEEP + "token");
        request.setToken(token);
        request.setPayId((String) mRootView.getCache().get("payId"));
        request.setAmount((long) mRootView.getCache().get("payMoney"));
        request.setOrderId(mRootView.getActivity().getIntent().getStringExtra("orderId"));
        mModel.pay(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<PayResponse>(mErrorHandler) {
                    @Override
                    public void onNext(PayResponse response) {
                        if (response.isSuccess()) {
                            // 正式支付
                            PayManager.getInstace(mRootView.getActivity()).toPay("ALIPAY_APP".equals(request.getPayId()) ? PayManager.PayMode.ALIPAY : PayManager.PayMode.WXPAY, response.getParams(), (PayManager.PayListener) mRootView.getActivity());
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }
}
