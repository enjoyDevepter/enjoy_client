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

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.HGoodsOrderConfirmContract;
import me.jessyan.mvparms.demo.mvp.model.entity.request.HGoodsOrderConfirmInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.HGoodsPayOrderRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HGoodsOrderConfirmInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HGoodsPayOrderResponse;
import me.jessyan.mvparms.demo.mvp.ui.activity.MainActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.PayActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.PayResultActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.SelfPickupAddrListActivity;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;
import static me.jessyan.mvparms.demo.mvp.ui.activity.HospitalInfoActivity.KEY_FOR_HOSPITAL_ID;


@ActivityScope
public class HGoodsOrderConfirmPresenter extends BasePresenter<HGoodsOrderConfirmContract.Model, HGoodsOrderConfirmContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;

    HGoodsOrderConfirmInfoResponse hGoodsOrderConfirmInfoResponse;
    private SelfPickupAddrListActivity.ListType listType = SelfPickupAddrListActivity.ListType.HOP;

    @Inject
    public HGoodsOrderConfirmPresenter(HGoodsOrderConfirmContract.Model model, HGoodsOrderConfirmContract.View rootView) {
        super(model, rootView);
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        getOrderConfirmInfo();
    }

    public void getOrderConfirmInfo() {

        HGoodsOrderConfirmInfoRequest request = new HGoodsOrderConfirmInfoRequest();
        boolean payAll = mRootView.getActivity().getIntent().getBooleanExtra("payAll", false);

        String where = mRootView.getActivity().getIntent().getStringExtra("where");
        if ("timelimitdetail".equals(where)) {
            request.setCmd(545);
        } else if ("newpeople".equals(where)) {
            request.setCmd(535);
        } else {
            if (payAll) {
                request.setCmd(525);
            } else {
                request.setCmd(523);
            }
        }
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        request.setProvince((String) (cache.get("province")));
        request.setCity((String) (cache.get("city")));
        request.setCounty((String) (cache.get("county")));
        if (mRootView.getCache().get("money") != null) {
            request.setMoney((Long) mRootView.getCache().get("money"));
        }
        request.setCouponId((String) mRootView.getCache().get("couponId"));
        request.setGoodsList(mRootView.getActivity().getIntent().getParcelableArrayListExtra("goodsList"));
        mModel.getOrderConfirmInfo(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<HGoodsOrderConfirmInfoResponse>(mErrorHandler) {
                    @Override
                    public void onNext(HGoodsOrderConfirmInfoResponse response) {
                        if (response.isSuccess()) {
                            hGoodsOrderConfirmInfoResponse = response;
                            mRootView.updateUI(response);
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });

    }


    public void placeHGoodsOrder() {
        HGoodsPayOrderRequest request = new HGoodsPayOrderRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        if (null == mRootView.getCache().get(KEY_FOR_HOSPITAL_ID)) {
            mRootView.showMessage("请选择医院！");
            return;
        }
        if (null == mRootView.getCache().get("hAppointments")) {
            mRootView.showMessage("请预约时间！");
            return;
        }
        if (null == mRootView.getCache().get("addressId")) {
            mRootView.showMessage("请选择地址！");
            return;
        }

        boolean payAll = mRootView.getActivity().getIntent().getBooleanExtra("payAll", false);
        String where = mRootView.getActivity().getIntent().getStringExtra("where");
        if ("timelimitdetail".equals(where)) {
            request.setCmd(546);
        } else if ("newpeople".equals(where)) {
            request.setCmd(536);
        } else {
            if (payAll) {
                request.setCmd(526);
            } else {
                request.setCmd(524);
            }
        }

        request.setMemberAddressId((String) mRootView.getCache().get("addressId"));
        request.setReservationDate((String) mRootView.getCache().get("appointmentsDate"));
        request.setReservationTime((String) mRootView.getCache().get("appointmentsTime"));
        request.setHospitalId((String) mRootView.getCache().get(KEY_FOR_HOSPITAL_ID));
        request.setCouponId((String) mRootView.getCache().get("couponId"));
        request.setCoupon(hGoodsOrderConfirmInfoResponse.getCoupon());
        request.setDeductionMoney(hGoodsOrderConfirmInfoResponse.getDeductionMoney());
        if (mRootView.getCache().get("money") != null) {
            request.setMoney((Long) mRootView.getCache().get("money"));
        }
        request.setFreight(hGoodsOrderConfirmInfoResponse.getFreight());
        request.setPrice(hGoodsOrderConfirmInfoResponse.getPrice());
        request.setTotalPrice(hGoodsOrderConfirmInfoResponse.getTotalPrice());
        request.setPayMoney(hGoodsOrderConfirmInfoResponse.getPayMoney());
        request.setRemark((String) mRootView.getCache().get("remark"));
        request.setToken(String.valueOf(cache.get(KEY_KEEP + "token")));
        request.setGoodsList(hGoodsOrderConfirmInfoResponse.getGoodsList());
        mRootView.showLoading();
        mModel.placeHGoodsOrder(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<HGoodsPayOrderResponse>(mErrorHandler) {
                    @Override
                    public void onNext(HGoodsPayOrderResponse response) {
                        mRootView.hideLoading();
                        if (response.isSuccess()) {
                            mAppManager.killAllBeforeClass(MainActivity.class);
                            if ("1".equals(response.getPayStatus())) {
                                Intent intent = new Intent(mRootView.getActivity(), PayActivity.class);
                                intent.putExtra("orderId", response.getOrderId());
                                ArmsUtils.startActivity(intent);
                            } else {
                                Intent intent = new Intent(mRootView.getActivity(), PayResultActivity.class);
                                intent.putExtra("orderId", response.getOrderId());
                                intent.putExtra("payMoney", response.getTotalPrice());
                                intent.putExtra("orderTime", response.getOrderTime());
                                intent.putExtra("orderType", response.getOrderType());
                                intent.putExtra("payTypeDesc", response.getPayTypeDesc());
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
