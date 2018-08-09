package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import me.jessyan.mvparms.demo.mvp.contract.CouponContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Coupon;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class CouponPresenter extends BasePresenter<CouponContract.Model, CouponContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    List<Coupon> couponList;
    @Inject
    RecyclerView.Adapter mAdapter;

    @Inject
    public CouponPresenter(CouponContract.Model model, CouponContract.View rootView) {
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

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        couponList.clear();
        couponList.addAll(mRootView.getActivity().getIntent().getParcelableArrayListExtra("coupons"));
        mAdapter.notifyDataSetChanged();
    }
}
