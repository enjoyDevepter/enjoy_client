package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.LogisticsContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.MainService;
import me.jessyan.mvparms.demo.mvp.model.entity.order.request.OrderOperationRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.order.response.LogisticsResponse;


@ActivityScope
public class LogisticsModel extends BaseModel implements LogisticsContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public LogisticsModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<LogisticsResponse> getLogistics(OrderOperationRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .getLogistics(request);
    }
}