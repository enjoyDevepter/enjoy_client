package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.KGoodsOrderConfirmContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.HGoodsService;
import me.jessyan.mvparms.demo.mvp.model.entity.request.HGoodsOrderConfirmInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.HGoodsPayOrderRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HGoodsOrderConfirmInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HGoodsPayOrderResponse;


@ActivityScope
public class KGoodsOrderConfirmModel extends BaseModel implements KGoodsOrderConfirmContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public KGoodsOrderConfirmModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
        super(repositoryManager);
        this.mGson = gson;
        this.mApplication = application;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<HGoodsOrderConfirmInfoResponse> getOrderConfirmInfo(HGoodsOrderConfirmInfoRequest request) {
        return mRepositoryManager.obtainRetrofitService(HGoodsService.class)
                .getOrderConfirmInfo(request);
    }

    @Override
    public Observable<HGoodsPayOrderResponse> placeHGoodsOrder(HGoodsPayOrderRequest request) {
        return mRepositoryManager.obtainRetrofitService(HGoodsService.class)
                .placeHGoodsOrder(request);
    }
}