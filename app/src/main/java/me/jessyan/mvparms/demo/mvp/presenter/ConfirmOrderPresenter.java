package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.ConfirmOrderContract;
import me.jessyan.mvparms.demo.mvp.model.entity.request.OrderConfirmInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.OrderConfirmInfoResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class ConfirmOrderPresenter extends BasePresenter<ConfirmOrderContract.Model, ConfirmOrderContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    RecyclerView.Adapter mAdapter;
    @Inject
    List<OrderConfirmInfoResponse.GoodsBean> goodsBeans;

    @Inject
    public ConfirmOrderPresenter(ConfirmOrderContract.Model model, ConfirmOrderContract.View rootView) {
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
        getOrderConfirmInfo();
    }

    private void getOrderConfirmInfo() {

        OrderConfirmInfoRequest request = new OrderConfirmInfoRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        request.setToken(String.valueOf(cache.get("token")));
        request.setProvince(String.valueOf(cache.get("province")));
        request.setCity(String.valueOf(cache.get("city")));
        request.setCounty(String.valueOf(cache.get("county")));
        request.setGoodsList(mRootView.getActivity().getIntent().getParcelableArrayListExtra("goodsList"));
        mRootView.showLoading();
        mModel.getOrderConfirmInfo(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<OrderConfirmInfoResponse>() {
                    @Override
                    public void accept(OrderConfirmInfoResponse response) throws Exception {
                        mRootView.hideLoading();
                        if (response.isSuccess()) {
                            goodsBeans.clear();
                            goodsBeans.addAll(response.getGoodsList());
                            mRootView.updateUI(response);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });

    }

}
