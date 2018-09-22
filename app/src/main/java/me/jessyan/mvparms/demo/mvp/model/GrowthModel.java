package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.GrowthContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.UserService;
import me.jessyan.mvparms.demo.mvp.model.entity.request.AddressListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.GrowthInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.GrowthInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.GrowthListResponse;


@ActivityScope
public class GrowthModel extends BaseModel implements GrowthContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public GrowthModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<GrowthInfoResponse> getGrowthInfo(GrowthInfoRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .getGrowthInfo(request);
    }

    @Override
    public Observable<GrowthListResponse> getGrowthList(AddressListRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .getGrowthList(request);
    }
}