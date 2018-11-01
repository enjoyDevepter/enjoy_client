package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.mvp.contract.MealOrderConfirmContract;
import me.jessyan.mvparms.demo.mvp.model.entity.request.MealOrderConfrimRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.PayMealOrderRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.MealOrderConfirmResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.PayMealOrderResponse;
import me.jessyan.mvparms.demo.mvp.ui.activity.MealOrderConfirmActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.PayActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.PayResultActivity;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


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

    public void getMealOrderConfirmInfo() {

        MealOrderConfrimRequest request = new MealOrderConfrimRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));

        MealOrderConfrimRequest.MealGoods mealGoods = new MealOrderConfrimRequest.MealGoods();
        if (mRootView.getCache().get("money") != null) {
            request.setMoney((Long) mRootView.getCache().get("money"));
        }
        mealGoods.setNums(mRootView.getActivity().getIntent().getIntExtra("nums", 1));
        mealGoods.setTotalPrice(mRootView.getActivity().getIntent().getDoubleExtra("totalPrice", 0));
        mealGoods.setSetMealId(mRootView.getActivity().getIntent().getStringExtra("setMealId"));
        mealGoods.setSalePrice(mRootView.getActivity().getIntent().getDoubleExtra("salePrice", 0));
        request.setSetMealGoods(mealGoods);
        mRootView.showLoading();
        mModel.getMealOrderConfirmInfo(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<MealOrderConfirmResponse>(mErrorHandler) {
                    @Override
                    public void onNext(MealOrderConfirmResponse response) {
                        mRootView.hideLoading();
                        if (response.isSuccess()) {
                            orderConfirmInfoResponse = response;
                            mRootView.updateUI(response);
                        }
                    }
                });
    }


    public void placeMealOrder() {
        PayMealOrderRequest request = new PayMealOrderRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        if (ArmsUtils.isEmpty((String) mRootView.getCache().get("addressId"))) {
            mRootView.showMessage("请选择地址！");
            return;
        }
        request.setMemberAddressId((String) mRootView.getCache().get("addressId"));
        request.setMoney(orderConfirmInfoResponse.getMoney());
        request.setPrice(orderConfirmInfoResponse.getPrice());
        request.setTotalPrice(orderConfirmInfoResponse.getTotalPrice());
        request.setPayMoney(orderConfirmInfoResponse.getPayMoney());
        request.setRemark((String) mRootView.getCache().get("remark"));
        request.setToken(String.valueOf(cache.get(KEY_KEEP + "token")));

        PayMealOrderRequest.MealGoods mealGoods = new PayMealOrderRequest.MealGoods();
        mealGoods.setNums(1);
        mealGoods.setSalePrice(orderConfirmInfoResponse.getSetMealGoods().getSalePrice());
        mealGoods.setSetMealId(orderConfirmInfoResponse.getSetMealGoods().getSetMealId());
        mealGoods.setTotalPrice(orderConfirmInfoResponse.getSetMealGoods().getTotalPrice());
        request.setSetMealGoods(mealGoods);

        mRootView.showLoading();
        mModel.placeMealOrder(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<PayMealOrderResponse>(mErrorHandler) {
                    @Override
                    public void onNext(PayMealOrderResponse response) {
                        mRootView.hideLoading();
                        if (response.isSuccess()) {
                            if ("0".equals(response.getPayStatus())) {
                                Intent intent = new Intent(mRootView.getActivity(), PayActivity.class);
                                intent.putExtra("orderId", response.getOrderId());
                                ArmsUtils.startActivity(intent);
                            } else {
                                EventBus.getDefault().post(EventBusTags.MEAL_PAY_SUCCESS);
                                Intent intent = new Intent(mRootView.getActivity(), PayResultActivity.class);
                                intent.putExtra("wait", false);
                                intent.putExtra("orderId", response.getOrderId());
                                intent.putExtra("payMoney", response.getTotalPrice());
                                intent.putExtra("orderTime", response.getOrderTime());
                                intent.putExtra("orderType", response.getOrderType());
                                intent.putExtra("payTypeDesc", response.getPayTypeDesc());
                                ArmsUtils.startActivity(intent);
                            }
                            mAppManager.killAllBeforeClass(MealOrderConfirmActivity.class);
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
