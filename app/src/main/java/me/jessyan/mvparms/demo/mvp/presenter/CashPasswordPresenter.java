package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.CashPasswordContract;
import me.jessyan.mvparms.demo.mvp.model.entity.request.VeritfyRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.SetCashPasswordRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.SetCashPasswordResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class CashPasswordPresenter extends BasePresenter<CashPasswordContract.Model, CashPasswordContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public CashPasswordPresenter(CashPasswordContract.Model model, CashPasswordContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public void setCashPassword(String confirmPayPwd, String payPwd, String verifycode) {
        SetCashPasswordRequest setCashPasswordRequest = new SetCashPasswordRequest();
        setCashPasswordRequest.setConfirmPayPwd(confirmPayPwd);
        setCashPasswordRequest.setPayPwd(payPwd);
        setCashPasswordRequest.setVerifyCode(verifycode);
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(ArmsUtils.getContext()).extras();
        setCashPasswordRequest.setToken((String) (cache.get(KEY_KEEP + "token")));

        mModel.setCashPassword(setCashPasswordRequest)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();//显示下拉刷新的进度条
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();//隐藏下拉刷新的进度条
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribe(new ErrorHandleSubscriber<SetCashPasswordResponse>(mErrorHandler) {
                    @Override
                    public void onNext(SetCashPasswordResponse response) {
                        if (response.isSuccess()) {
                            mRootView.showOk();
                        }
                    }
                });
    }

    public void getVerifyForFind() {
        VeritfyRequest request = new VeritfyRequest();
        request.setCmd(110);
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        String token = String.valueOf(cache.get(KEY_KEEP + "token"));
        request.setToken(token);

        mModel.getVerifyForFind(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (!response.isSuccess()) {
                            mRootView.showVerity();
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

}
