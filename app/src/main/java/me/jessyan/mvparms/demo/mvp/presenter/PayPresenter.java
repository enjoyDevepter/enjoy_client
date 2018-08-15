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

import me.jessyan.mvparms.demo.mvp.contract.PayContract;
import me.jessyan.mvparms.demo.mvp.model.entity.PayGoods;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


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
    @Inject
    List<PayGoods> goodsList;
    @Inject
    RecyclerView.Adapter mAdapter;

    @Inject
    public PayPresenter(PayContract.Model model, PayContract.View rootView) {
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
        goodsList.addAll(mRootView.getActivity().getIntent().getParcelableArrayListExtra("goodsList"));
        mAdapter.notifyDataSetChanged();
    }
}
