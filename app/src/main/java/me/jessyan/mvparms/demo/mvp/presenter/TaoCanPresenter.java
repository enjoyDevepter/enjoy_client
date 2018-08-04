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

import me.jessyan.mvparms.demo.mvp.contract.TaoCanContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class TaoCanPresenter extends BasePresenter<TaoCanContract.Model, TaoCanContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    List<Goods> mGoods;
    @Inject
    RecyclerView.Adapter mAdapter;

    @Inject
    public TaoCanPresenter(TaoCanContract.Model model, TaoCanContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        getTaoCan(); // 进入界面后加载
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public void getTaoCan() {

    }

}
