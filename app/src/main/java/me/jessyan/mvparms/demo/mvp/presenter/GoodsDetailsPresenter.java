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
import com.zhy.view.flowlayout.TagAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.GoodsDetailsContract;
import me.jessyan.mvparms.demo.mvp.model.entity.GoodsSpecValue;
import me.jessyan.mvparms.demo.mvp.model.entity.Promotion;
import me.jessyan.mvparms.demo.mvp.model.entity.request.AddGoodsToCartRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.CollectGoodsRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.GoodsDetailsRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.OrderConfirmInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.GoodsDetailsResponse;
import me.jessyan.mvparms.demo.mvp.ui.activity.ConfirmOrderActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.LoginActivity;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class GoodsDetailsPresenter extends BasePresenter<GoodsDetailsContract.Model, GoodsDetailsContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    GoodsDetailsResponse goodsDetailsResponse;
    @Inject
    List<Promotion> promotionList;
    @Inject
    List<GoodsSpecValue> goodsSpecValues;
    @Inject
    TagAdapter adapter;

    @Inject
    public GoodsDetailsPresenter(GoodsDetailsContract.Model model, GoodsDetailsContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        getGoodsDetails();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    /**
     * 获取商品详情
     */
    public void getGoodsDetails() {

        GoodsDetailsRequest request = new GoodsDetailsRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setCmd(403);
        request.setCity((String) (cache.get("city")));
        request.setCounty((String) (cache.get("county")));
        request.setProvince((String) (cache.get("province")));
        request.setGoodsId(mRootView.getActivity().getIntent().getStringExtra("goodsId"));
        request.setMerchId(mRootView.getActivity().getIntent().getStringExtra("merchId"));
        mModel.getGoodsDetails(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GoodsDetailsResponse>() {
                    @Override
                    public void accept(GoodsDetailsResponse response) throws Exception {
                        if (response.isSuccess()) {
                            goodsDetailsResponse = response;
                            promotionList.clear();
                            promotionList.addAll(response.getPromotionList());
                            goodsSpecValues.clear();
                            goodsSpecValues.addAll(response.getGoodsSpecValueList());
                            Cache<String, Object> cache = mRootView.getCache();
                            cache.put("goods", response.getGoods());
                            adapter.notifyDataChanged();
                            mRootView.updateUI(response);
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    public void collectGoods(boolean collect) {
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        if (cache.get(KEY_KEEP + "token") == null) {
            ArmsUtils.startActivity(LoginActivity.class);
            return;
        }

        CollectGoodsRequest request = new CollectGoodsRequest();
        request.setCmd(collect ? 407 : 408);
        request.setToken((String) cache.get(KEY_KEEP + "token"));
        request.setGoodsId(mRootView.getActivity().getIntent().getStringExtra("goodsId"));
        request.setMerchId(mRootView.getActivity().getIntent().getStringExtra("merchId"));
        mModel.collectGoods(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(BaseResponse response) throws Exception {
                        if (response.isSuccess()) {
                            mRootView.updateCollect(collect);
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });

    }


    /**
     * 更加规格获取商品详情
     */
    public void getCoodsDetailsForSpecValueId() {
        GoodsDetailsRequest request = new GoodsDetailsRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setCmd(409);
        request.setCity((String) (cache.get("city")));
        request.setCounty((String) (cache.get("county")));
        request.setProvince((String) (cache.get("province")));
        request.setGoodsId(mRootView.getActivity().getIntent().getStringExtra("goodsId"));
        request.setMerchId(mRootView.getActivity().getIntent().getStringExtra("merchId"));
        request.setSpecValueId((String) (mRootView.getCache().get("specValueId")));
        mModel.getGoodsDetails(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GoodsDetailsResponse>() {
                    @Override
                    public void accept(GoodsDetailsResponse response) throws Exception {
                        if (response.isSuccess()) {
                            goodsDetailsResponse = response;
                            promotionList.clear();
                            promotionList.addAll(response.getPromotionList());
                            goodsSpecValues.clear();
                            goodsSpecValues.addAll(response.getGoodsSpecValueList());
                            Cache<String, Object> cache = mRootView.getCache();
                            cache.put("goods", response.getGoods());
                            adapter.notifyDataChanged();
                            mRootView.updateUI(response);
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    /**
     * 添加购物车
     */
    public void addGoodsToCart() {
        Cache<String, Object> appCache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        if (ArmsUtils.isEmpty((String) appCache.get(KEY_KEEP + "token"))) {
            ArmsUtils.startActivity(LoginActivity.class);
            return;
        }
        AddGoodsToCartRequest request = new AddGoodsToCartRequest();
        request.setGoodsId(mRootView.getActivity().getIntent().getStringExtra("goodsId"));
        request.setMerchId(mRootView.getActivity().getIntent().getStringExtra("merchId"));
        request.setNums(1);
        request.setPromotionId((String) mRootView.getCache().get("promotionId"));
        request.setToken((String) appCache.get(KEY_KEEP + "token"));
        mModel.addGoodsToCart(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(BaseResponse response) throws Exception {
                        if (response.isSuccess()) {
                            mRootView.showMessage(response.getRetDesc());
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    public void goOrderConfirm() {
        Cache<String, Object> appCache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        if (ArmsUtils.isEmpty((String) appCache.get(KEY_KEEP + "token"))) {
            ArmsUtils.startActivity(LoginActivity.class);
            return;
        }


        Intent intent = new Intent(mRootView.getActivity(), ConfirmOrderActivity.class);
        ArrayList<OrderConfirmInfoRequest.OrderGoods> goodsList = new ArrayList<>();
        OrderConfirmInfoRequest.OrderGoods goods = new OrderConfirmInfoRequest.OrderGoods();
        goods.setGoodsId(goodsDetailsResponse.getGoods().getGoodsId());
        goods.setMerchId(goodsDetailsResponse.getGoods().getMerchId());
        goods.setNums(1);
        goods.setSalePrice(goodsDetailsResponse.getGoods().getSalePrice());
        goods.setPromotionId((String) mRootView.getCache().get("promotionId"));
        goodsList.add(goods);
        intent.putParcelableArrayListExtra("goodsList", goodsList);
        ArmsUtils.startActivity(intent);


//        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
//        request.setToken((String) (cache.get(KEY_KEEP+"token")));
//        request.setProvince((String) (cache.get("province")));
//        request.setCity((String) (cache.get("city")));
//        request.setCounty((String) (cache.get("county")));
//        request.setDeliveryMethod((String) mRootView.getCache().get("deliveryMethod"));
//        if (mRootView.getCache().get("money") != null) {
//            request.setMoney((Long) mRootView.getCache().get("money"));
//        }
//        request.setCouponId((String) mRootView.getCache().get("couponId"));
//        request.setGoodsList((List<OrderConfirmInfoRequest.OrderGoods>) mRootView.getCache().get("goodsList"));

    }

}
