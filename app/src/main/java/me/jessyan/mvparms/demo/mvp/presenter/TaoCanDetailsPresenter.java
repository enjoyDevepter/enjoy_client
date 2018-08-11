package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
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
import me.jessyan.mvparms.demo.mvp.contract.TaoCanDetailsContract;
import me.jessyan.mvparms.demo.mvp.model.entity.MealGoods;
import me.jessyan.mvparms.demo.mvp.model.entity.request.MealDetailsRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.MealDetailsResponse;
import me.jessyan.mvparms.demo.mvp.ui.activity.LoginActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.MealOrderConfirmActivity;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class TaoCanDetailsPresenter extends BasePresenter<TaoCanDetailsContract.Model, TaoCanDetailsContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    List<MealGoods.Goods> goodsList;
    @Inject
    RecyclerView.Adapter mAdapter;
    MealDetailsResponse mealDetailsResponse;

    @Inject
    public TaoCanDetailsPresenter(TaoCanDetailsContract.Model model, TaoCanDetailsContract.View rootView) {
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
        getMealDetail();
    }

    private void getMealDetail() {
        MealDetailsRequest request = new MealDetailsRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        if (cache.get("token") != null) {
            request.setCmd(432);
        } else {
            request.setCmd(431);
        }
        request.setSetMealId(mRootView.getActivity().getIntent().getStringExtra("setMealId"));
        mModel.getMealDetail(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MealDetailsResponse>() {
                    @Override
                    public void accept(MealDetailsResponse response) throws Exception {
                        if (response.isSuccess()) {
                            goodsList.clear();
                            goodsList.addAll(response.getSetMealGoods().getGoodsList());
                            mRootView.updateUI(response);
                            mealDetailsResponse = response;
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    public void buy() {
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        if (cache.get("token") == null) {
            ArmsUtils.startActivity(LoginActivity.class);
            return;
        }
        Intent intent = new Intent(mRootView.getActivity(), MealOrderConfirmActivity.class);
        intent.putExtra("totalPrice", mealDetailsResponse.getSetMealGoods().getTotalPrice());
        intent.putExtra("setMealId", mealDetailsResponse.getSetMealGoods().getSetMealId());
        intent.putExtra("salePrice", mealDetailsResponse.getSetMealGoods().getSalesPrice());
        ArmsUtils.startActivity(intent);

    }

}
