package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.MainContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.MainService;
import me.jessyan.mvparms.demo.mvp.model.api.service.UserService;
import me.jessyan.mvparms.demo.mvp.model.entity.request.HomeADRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.CityResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HomeAdResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.LocationRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.UpdateRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.UserInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.UpdateResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.UserInfoResponse;


@ActivityScope
public class MainModel extends BaseModel implements MainContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public MainModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<UpdateResponse> checkUpdate(UpdateRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .checkUpdate(request);
    }

    @Override
    public Observable<CityResponse> getAreaForLoaction(LocationRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .getAreaForLoaction(request);
    }

    @Override
    public Observable<HomeAdResponse> getOrCancelAD(HomeADRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .getOrCancelAD(request);
    }

    @Override
    public Observable<UserInfoResponse> getSignStatus(UserInfoRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .getUserInfo(request);
    }
}