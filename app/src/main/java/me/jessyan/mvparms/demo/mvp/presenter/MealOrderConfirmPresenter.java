package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.os.Parcelable;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.MealOrderConfirmContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Address;
import me.jessyan.mvparms.demo.mvp.model.entity.request.MealOrderConfrimRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.PayMealOrderRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.MealOrderConfirmResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.PayMealOrderResponse;
import me.jessyan.mvparms.demo.mvp.ui.activity.PayActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.PayResultActivity;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class MealOrderConfirmPresenter extends BasePresenter<MealOrderConfirmContract.Model, MealOrderConfirmContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;

    private MealOrderConfirmResponse orderConfirmInfoResponse;


    @Inject
    public MealOrderConfirmPresenter(MealOrderConfirmContract.Model model, MealOrderConfirmContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        cache.put("memberAddressInfo", null);
        getMealOrderConfirmInfo();
    }

    private void getMealOrderConfirmInfo() {

        MealOrderConfrimRequest request = new MealOrderConfrimRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        request.setToken((String) (cache.get("token")));

        MealOrderConfrimRequest.MealGoods mealGoods = new MealOrderConfrimRequest.MealGoods();
        mealGoods.setNums(mRootView.getActivity().getIntent().getIntExtra("nums", 1));
        mealGoods.setTotalPrice(mRootView.getActivity().getIntent().getDoubleExtra("totalPrice", 0));
        mealGoods.setSetMealId(mRootView.getActivity().getIntent().getStringExtra("setMealId"));
        mealGoods.setSalePrice(mRootView.getActivity().getIntent().getDoubleExtra("salePrice", 0));
        request.setSetMealGoods(mealGoods);
        mRootView.showLoading();
        mModel.getMealOrderConfirmInfo(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MealOrderConfirmResponse>() {
                    @Override
                    public void accept(MealOrderConfirmResponse response) throws Exception {
                        mRootView.hideLoading();
                        if (response.isSuccess()) {
                            orderConfirmInfoResponse = response;
                            mRootView.updateUI(response);
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });

    }


    public void placeMealOrder() {
        PayMealOrderRequest request = new PayMealOrderRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        if (null == cache.get("memberAddressInfo")) {
            mRootView.showMessage("请选择地址！");
            return;
        }
        Address address = (Address) cache.get("memberAddressInfo");
        request.setMemberAddressId(address.getAddressId());

        if (mRootView.getCache().get("money") != null) {
            request.setMoney((Long) mRootView.getCache().get("money"));
        }
        request.setPrice(orderConfirmInfoResponse.getPrice());
        request.setTotalPrice(orderConfirmInfoResponse.getTotalPrice());
        request.setPayMoney(orderConfirmInfoResponse.getPayMoney());
        request.setRemark((String) mRootView.getCache().get("remark"));
        request.setToken(String.valueOf(cache.get("token")));

        PayMealOrderRequest.MealGoods mealGoods = new PayMealOrderRequest.MealGoods();
        mealGoods.setNums(1);
        mealGoods.setSalePrice(orderConfirmInfoResponse.getSetMealGoods().getSalesPrice());
        mealGoods.setSetMealId(orderConfirmInfoResponse.getSetMealGoods().getSetMealId());
        mealGoods.setTotalPrice(orderConfirmInfoResponse.getSetMealGoods().getTotalPrice());
        request.setSetMealGoods(mealGoods);

        mRootView.showLoading();
        mModel.placeMealOrder(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PayMealOrderResponse>() {
                    @Override
                    public void accept(PayMealOrderResponse response) throws Exception {
                        mRootView.hideLoading();
                        if (response.isSuccess()) {
                            if ("0".equals(response.getPayStatus())) {
                                Intent intent = new Intent(mRootView.getActivity(), PayActivity.class);
                                intent.putExtra("comefrom", "meal");
                                intent.putExtra("orderId", response.getOrderId());
                                intent.putExtra("payMoney", response.getPayMoney());
                                intent.putExtra("orderTime", response.getOrderTime());
                                intent.putParcelableArrayListExtra("payEntryList", (ArrayList<? extends Parcelable>) response.getPayEntryList());
                                ArmsUtils.startActivity(intent);
                            } else {
                                Intent intent = new Intent(mRootView.getActivity(), PayResultActivity.class);
                                intent.putExtra("orderId", response.getOrderId());
                                intent.putExtra("payMoney", response.getPayMoney());
                                intent.putExtra("orderTime", response.getOrderTime());
                                List<PayMealOrderResponse.PayEntry> payEntryList = response.getPayEntryList();
                                if (payEntryList != null && payEntryList.size() > 0) {
                                    intent.putExtra("payName", payEntryList.get(0).getName());
                                }
                                ArmsUtils.startActivity(intent);
                            }
                        } else {
                            mRootView.showMessage(response.getRetDesc());
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
