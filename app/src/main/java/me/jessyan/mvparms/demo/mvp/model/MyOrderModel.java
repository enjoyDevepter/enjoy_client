package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.MyOrderContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.MainService;
import me.jessyan.mvparms.demo.mvp.model.entity.order.request.OrderOperationRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.OrderRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.OrderResponse;


@ActivityScope
public class MyOrderModel extends BaseModel implements MyOrderContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public MyOrderModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<OrderResponse> getMyOrder(OrderRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .getMyOrder(request);
    }

    @Override
    public Observable<BaseResponse> cancelOrder(OrderOperationRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .cancelOrder(request);
    }
}