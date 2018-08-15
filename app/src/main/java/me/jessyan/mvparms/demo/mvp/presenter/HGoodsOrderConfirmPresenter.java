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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.HGoodsOrderConfirmContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Address;
import me.jessyan.mvparms.demo.mvp.model.entity.Store;
import me.jessyan.mvparms.demo.mvp.model.entity.request.HGoodsOrderConfirmInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.HGoodsPayOrderRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HGoodsOrderConfirmInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HGoodsPayOrderResponse;
import me.jessyan.mvparms.demo.mvp.ui.activity.PayActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.SelfPickupAddrListActivity;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


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
    private SelfPickupAddrListActivity.ListType listType = SelfPickupAddrListActivity.ListType.ADDR;

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
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        request.setToken((String) (cache.get("token")));
        request.setProvince((String) (cache.get("province")));
        request.setCity((String) (cache.get("city")));
        request.setCounty((String) (cache.get("county")));
        if (mRootView.getCache().get("money") != null) {
            request.setMoney((Long) mRootView.getCache().get("money"));
        }
        request.setCouponId((String) mRootView.getCache().get("couponId"));
        List<HGoodsOrderConfirmInfoRequest.HGoods> hGoodsList = new ArrayList<>();
        HGoodsOrderConfirmInfoRequest.HGoods goods = new HGoodsOrderConfirmInfoRequest.HGoods();
        goods.setGoodsId(mRootView.getActivity().getIntent().getStringExtra("goodsId"));
        goods.setMerchId(mRootView.getActivity().getIntent().getStringExtra("merchId"));
        goods.setAdvanceDepositId(mRootView.getActivity().getIntent().getStringExtra("advanceDepositId"));
        goods.setDeposit(mRootView.getActivity().getIntent().getDoubleExtra("deposit", 0));
        goods.setTailMoney(mRootView.getActivity().getIntent().getDoubleExtra("tailMoney", 0));
        goods.setNums(mRootView.getActivity().getIntent().getIntExtra("nums", 1));
        goods.setPromotionId(mRootView.getActivity().getIntent().getStringExtra("promotionId"));
        goods.setSalePrice(mRootView.getActivity().getIntent().getDoubleExtra("salePrice", 0));
        hGoodsList.add(goods);
        request.setGoodsList(hGoodsList);
        mModel.getOrderConfirmInfo(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HGoodsOrderConfirmInfoResponse>() {
                    @Override
                    public void accept(HGoodsOrderConfirmInfoResponse response) throws Exception {
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
        }
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
        request.setToken(String.valueOf(cache.get("token")));
        request.setGoodsList((List<HGoodsPayOrderRequest.OrderGoods>) mRootView.getCache().get("goodsList"));
        mRootView.showLoading();
        mModel.placeHGoodsOrder(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HGoodsPayOrderResponse>() {
                    @Override
                    public void accept(HGoodsPayOrderResponse response) throws Exception {
                        mRootView.hideLoading();
                        if (response.isSuccess()) {
                            if ("0".equals(response.getPayStatus())) {
                                Intent intent = new Intent(mRootView.getActivity(), PayActivity.class);
                                intent.putExtra("comefrom", "mall");
                                intent.putExtra("orderId", response.getOrderId());
                                intent.putExtra("payMoney", response.getPayMoney());
                                intent.putExtra("orderTime", response.getOrderTime());
//                                intent.putParcelableArrayListExtra("goodsList", (ArrayList<? extends Parcelable>) response.getGoodsList());
//                                intent.putParcelableArrayListExtra("payEntryList", (ArrayList<? extends Parcelable>) response.getPayEntryList());
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
