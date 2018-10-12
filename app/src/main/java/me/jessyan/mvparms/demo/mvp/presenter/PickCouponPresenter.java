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

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.PickCouponContract;
import me.jessyan.mvparms.demo.mvp.model.entity.PickCoupon;
import me.jessyan.mvparms.demo.mvp.model.entity.request.PickCouponRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.PickCouponListResponse;
import me.jessyan.mvparms.demo.mvp.ui.adapter.PickCouponListAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class PickCouponPresenter extends BasePresenter<PickCouponContract.Model, PickCouponContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    List<PickCoupon> couponList;
    @Inject
    PickCouponListAdapter mAdapter;

    @Inject
    public PickCouponPresenter(PickCouponContract.Model model, PickCouponContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        getPickCouponList();
    }

    private void getPickCouponList() {
        PickCouponRequest request = new PickCouponRequest();
        request.setCmd(918);
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        mModel.getPickCouponList(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<PickCouponListResponse>(mErrorHandler) {
                    @Override
                    public void onNext(PickCouponListResponse response) {
                        if (response.isSuccess()) {
                            couponList.clear();
                            couponList.addAll(response.getCouponList());
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public void pickCoupon() {
        PickCouponRequest request = new PickCouponRequest();
        request.setCmd(919);
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        request.setCouponTemplateId((String) mRootView.getCache().get("couponTemplateId"));
        request.setCouponPromotionId((String) mRootView.getCache().get("couponPromotionId"));
        mModel.pickCoupon(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.isSuccess()) {
                            getPickCouponList();
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
