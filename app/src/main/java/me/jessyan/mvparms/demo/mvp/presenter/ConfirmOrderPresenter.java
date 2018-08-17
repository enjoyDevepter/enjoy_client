package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;

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
import me.jessyan.mvparms.demo.mvp.contract.ConfirmOrderContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Address;
import me.jessyan.mvparms.demo.mvp.model.entity.Store;
import me.jessyan.mvparms.demo.mvp.model.entity.request.OrderConfirmInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.PayOrderRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.OrderConfirmInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.PayOrderResponse;
import me.jessyan.mvparms.demo.mvp.ui.activity.PayActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.SelfPickupAddrListActivity;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


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

    private OrderConfirmInfoResponse orderConfirmInfoResponse;

    private SelfPickupAddrListActivity.ListType listType = SelfPickupAddrListActivity.ListType.ADDR;

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

    public void getOrderConfirmInfo() {

        OrderConfirmInfoRequest request = new OrderConfirmInfoRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        request.setProvince((String) (cache.get("province")));
        request.setCity((String) (cache.get("city")));
        request.setCounty((String) (cache.get("county")));
        request.setDeliveryMethod((String) mRootView.getCache().get("deliveryMethod"));
        if (mRootView.getCache().get("money") != null) {
            request.setMoney((Long) mRootView.getCache().get("money"));
        }
        request.setCouponId((String) mRootView.getCache().get("couponId"));
        request.setGoodsList((List<OrderConfirmInfoRequest.OrderGoods>) mRootView.getCache().get("goodsList"));
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
                            orderConfirmInfoResponse = response;
                            mRootView.updateUI(response);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });

    }


    public void placeOrder() {
        PayOrderRequest request = new PayOrderRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        if ("1".equals(mRootView.getCache().get("deliveryMethod"))) {
            if (null == cache.get("memberAddressInfo")) {
                mRootView.showMessage("请选择地址！");
                return;
            }
            Address address = (Address) cache.get("memberAddressInfo");
            request.setMemberAddressId(address.getAddressId());
        } else {
            if (null == cache.get(listType.getDataKey())) {
                mRootView.showMessage("请选择自取店铺！");
                return;
            }
            Store store = (Store) cache.get(listType.getDataKey());
            request.setStoreId(store.getId());
        }
        request.setDeliveryMethodId((String) mRootView.getCache().get("deliveryMethodId"));
        request.setCouponId((String) mRootView.getCache().get("couponId"));
        request.setCoupon(orderConfirmInfoResponse.getCoupon());
        request.setDeductionMoney(orderConfirmInfoResponse.getDeductionMoney());
        if (mRootView.getCache().get("money") != null) {
            request.setMoney((Long) mRootView.getCache().get("money"));
        }
        request.setFreight(orderConfirmInfoResponse.getFreight());
        request.setPrice(orderConfirmInfoResponse.getPrice());
        request.setTotalPrice(orderConfirmInfoResponse.getTotalPrice());
        request.setPayMoney(orderConfirmInfoResponse.getPayMoney());
        request.setRemark((String) mRootView.getCache().get("remark"));
        request.setToken(String.valueOf(cache.get(KEY_KEEP + "token")));
        request.setGoodsList((List<OrderConfirmInfoRequest.OrderGoods>) mRootView.getCache().get("goodsList"));
        mRootView.showLoading();
        mModel.placeOrder(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PayOrderResponse>() {
                    @Override
                    public void accept(PayOrderResponse response) throws Exception {
                        mRootView.hideLoading();
                        if (response.isSuccess()) {
                            if ("0".equals(response.getPayStatus())) {
                                Intent intent = new Intent(mRootView.getActivity(), PayActivity.class);
                                intent.putExtra("comefrom", "mall");
                                intent.putExtra("orderId", response.getOrderId());
                                intent.putExtra("payMoney", response.getPayMoney());
                                intent.putExtra("orderTime", response.getOrderTime());
                                intent.putParcelableArrayListExtra("goodsList", (ArrayList<? extends Parcelable>) response.getGoodsList());
                                intent.putParcelableArrayListExtra("payEntryList", (ArrayList<? extends Parcelable>) response.getPayEntryList());
                                ArmsUtils.startActivity(intent);
                            }
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

}
