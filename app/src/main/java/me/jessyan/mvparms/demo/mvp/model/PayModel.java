package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.PayContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.MainService;
import me.jessyan.mvparms.demo.mvp.model.entity.pay.request.PayInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.pay.request.PayRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.pay.response.PayInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.pay.response.PayResponse;


@ActivityScope
public class PayModel extends BaseModel implements PayContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public PayModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<PayInfoResponse> getOrderPayInfo(PayInfoRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .getOrderPayInfo(request);
    }

    @Override
    public Observable<PayInfoResponse> getPayStatus(PayInfoRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .getPayStatus(request);
    }

    @Override
    public Observable<PayResponse> pay(PayRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .pay(request);
    }
}