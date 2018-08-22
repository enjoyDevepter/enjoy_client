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
import me.jessyan.mvparms.demo.mvp.contract.HGoodsDetailsContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.model.entity.GoodsSpecValue;
import me.jessyan.mvparms.demo.mvp.model.entity.Promotion;
import me.jessyan.mvparms.demo.mvp.model.entity.request.GoodsDetailsRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HGoodsDetailsResponse;
import me.jessyan.mvparms.demo.mvp.ui.activity.ConfirmOrderActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.LoginActivity;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsPromotionAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class HGoodsDetailsPresenter extends BasePresenter<HGoodsDetailsContract.Model, HGoodsDetailsContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    List<Promotion> promotionList;
    @Inject
    List<GoodsSpecValue> goodsSpecValues;
    @Inject
    TagAdapter adapter;
    @Inject
    GoodsPromotionAdapter goodsPromotionAdapter;

    HGoodsDetailsResponse hGoodsDetailsResponse;

    @Inject
    public HGoodsDetailsPresenter(HGoodsDetailsContract.Model model, HGoodsDetailsContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        getHGoodsDetails();
    }

    /**
     * 获取商品详情
     */
    public void getHGoodsDetails() {

        GoodsDetailsRequest request = new GoodsDetailsRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        String token = (String) cache.get(KEY_KEEP + "token");
        String promotionId = mRootView.getActivity().getIntent().getStringExtra("promotionId");
        String where = mRootView.getActivity().getIntent().getStringExtra("where");
        if ("timelimitdetail".equals(where)) {
            request.setPromotionId(promotionId);
            if (ArmsUtils.isEmpty(token)) {
                request.setCmd(465);
            } else {
                request.setCmd(466);
            }
        } else if ("newpeople".equals(where)) {
            request.setPromotionId(promotionId);
            if (ArmsUtils.isEmpty(token)) {
                request.setCmd(455);
            } else {
                request.setCmd(456);
            }
        } else {
            if (ArmsUtils.isEmpty(token)) {
                request.setCmd(441);
            } else {
                request.setCmd(442);
            }
        }
        request.setToken(token);
        request.setCity((String) (cache.get("city")));
        request.setCounty((String) (cache.get("county")));
        request.setProvince((String) (cache.get("province")));
        request.setGoodsId(mRootView.getActivity().getIntent().getStringExtra("goodsId"));
        request.setMerchId(mRootView.getActivity().getIntent().getStringExtra("merchId"));
        request.setAdvanceDepositId(mRootView.getActivity().getIntent().getStringExtra("advanceDepositId"));
        mModel.getHGoodsDetails(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HGoodsDetailsResponse>() {
                    @Override
                    public void accept(HGoodsDetailsResponse response) throws Exception {
                        if (response.isSuccess()) {
                            hGoodsDetailsResponse = response;
                            promotionList.clear();
                            if (null != response.getPromotionList()) {
                                promotionList.addAll(response.getPromotionList());
                            }
                            goodsSpecValues.clear();
                            goodsSpecValues.addAll(response.getGoodsSpecValueList());
                            mRootView.updateUI(response);
                            adapter.notifyDataChanged();
                            goodsPromotionAdapter.notifyDataSetChanged();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    public void getHCoodsDetailsForSpecValueId() {
        GoodsDetailsRequest request = new GoodsDetailsRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        String token = (String) cache.get(KEY_KEEP + "token");
        if (ArmsUtils.isEmpty(token)) {
            request.setCmd(443);
        } else {
            request.setCmd(444);
        }
        request.setToken(token);
        request.setCity((String) (cache.get("city")));
        request.setCounty((String) (cache.get("county")));
        request.setProvince((String) (cache.get("province")));
        request.setGoodsId(mRootView.getActivity().getIntent().getStringExtra("goodsId"));
        request.setMerchId(mRootView.getActivity().getIntent().getStringExtra("merchId"));
        request.setSpecValueId((String) (mRootView.getCache().get("specValueId")));
        request.setAdvanceDepositId(mRootView.getActivity().getIntent().getStringExtra("advanceDepositId"));
        mModel.getHGoodsDetails(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HGoodsDetailsResponse>() {
                    @Override
                    public void accept(HGoodsDetailsResponse response) throws Exception {
                        if (response.isSuccess()) {
                            hGoodsDetailsResponse = response;
                            promotionList.clear();
                            promotionList.addAll(response.getPromotionList());
                            goodsSpecValues.clear();
                            goodsSpecValues.addAll(response.getGoodsSpecValueList());
                            mRootView.updateUI(response);
                            adapter.notifyDataChanged();
                            goodsPromotionAdapter.notifyDataSetChanged();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    public void goOrderConfirm() {
        String specValueId = (String) mRootView.getCache().get("specValueId");
        if (ArmsUtils.isEmpty(specValueId)) {
            mRootView.showMessage("请选择规格！");
            return;
        }

        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        String token = (String) cache.get(KEY_KEEP + "token");
        if (ArmsUtils.isEmpty(token)) {
            ArmsUtils.startActivity(LoginActivity.class);
            return;
        }

        Intent intent = new Intent(mRootView.getActivity(), ConfirmOrderActivity.class);
        ArrayList<Goods> goodsList = new ArrayList<>();
        Goods goods = hGoodsDetailsResponse.getGoods();
        goods.setNums(1);
        goodsList.add(goods);
        intent.putExtra("where", mRootView.getActivity().getIntent().getStringExtra("where"));
        intent.putParcelableArrayListExtra("goodsList", goodsList);
        ArmsUtils.startActivity(intent);
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
