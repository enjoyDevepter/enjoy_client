package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.ConfirmOrderContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.MainService;
import me.jessyan.mvparms.demo.mvp.model.entity.request.OrderConfirmInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.PayOrderRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.OrderConfirmInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.PayOrderResponse;


@ActivityScope
public class ConfirmOrderModel extends BaseModel implements ConfirmOrderContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public ConfirmOrderModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<OrderConfirmInfoResponse> getOrderConfirmInfo(OrderConfirmInfoRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .getOrderConfirmInfo(request);
    }

    @Override
    public Observable<PayOrderResponse> placeOrder(PayOrderRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .placeOrder(request);
    }
}
