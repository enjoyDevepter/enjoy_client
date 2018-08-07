package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.GoodsDetailsContract;
import me.jessyan.mvparms.demo.mvp.model.entity.GoodsDetails;
import me.jessyan.mvparms.demo.mvp.model.entity.request.AddGoodsToCartRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.GoodsDetailsRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.GoodsDetailsResponse;
import me.jessyan.mvparms.demo.mvp.ui.activity.LoginActivity;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


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
    GoodsDetailsResponse response;
    @Inject
    List<GoodsDetails.Promotion> promotionList;
    @Inject
    List<GoodsDetails.GoodsSpecValue> goodsSpecValues;
    @Inject
    TagAdapter adapter;

    @Inject
    public GoodsDetailsPresenter(GoodsDetailsContract.Model model, GoodsDetailsContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        getGoodsDetails(mRootView.getActivity().getIntent().getStringExtra("goodsId"), mRootView.getActivity().getIntent().getStringExtra("merchId"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public void getGoodsDetails(String goodsId, String merchId) {

        GoodsDetailsRequest request = new GoodsDetailsRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setCity((String) (cache.get("city")));
        request.setCounty((String) (cache.get("county")));
        request.setProvince((String) (cache.get("province")));

        request.setGoodsId(goodsId);
        request.setMerchId(merchId);
        mModel.getGoodsDetails(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GoodsDetailsResponse>() {
                    @Override
                    public void accept(GoodsDetailsResponse response) throws Exception {
                        if (response.isSuccess()) {
                            GoodsDetailsPresenter.this.response = response;
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
        if (ArmsUtils.isEmpty((String) appCache.get("token"))) {
            ArmsUtils.startActivity(LoginActivity.class);
            return;
        }
        AddGoodsToCartRequest request = new AddGoodsToCartRequest();
        request.setGoodsId(mRootView.getActivity().getIntent().getStringExtra("goodsId"));
        request.setMerchId(mRootView.getActivity().getIntent().getStringExtra("merchId"));
        request.setNums(1);
        request.setPromotionId(mRootView.getActivity().getIntent().getStringExtra("promotionId"));
        request.setToken((String) appCache.get("token"));
        mModel.addGoodsToCart(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(BaseResponse response) throws Exception {
                        if (response.isSuccess()) {

                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

}
