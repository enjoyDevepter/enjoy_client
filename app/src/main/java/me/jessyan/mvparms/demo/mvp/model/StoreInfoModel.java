package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.StoreInfoContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.HospitalService;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.request.ActivityInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.response.ActivityInfoListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.request.GoodsListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.StoreInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.GoodsListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.StoreInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.FollowRequest;


@ActivityScope
public class StoreInfoModel extends BaseModel implements StoreInfoContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public StoreInfoModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<BaseResponse> follow(FollowRequest request) {
        return mRepositoryManager.obtainRetrofitService(HospitalService.class)
                .follow(request);
    }

    @Override
    public Observable<ActivityInfoListResponse> getActivityList(ActivityInfoRequest request) {
        return mRepositoryManager.obtainRetrofitService(HospitalService.class)
                .getActivityList(request);
    }

    @Override
    public Observable<StoreInfoResponse> getStoreInfo(StoreInfoRequest request) {
        return mRepositoryManager.obtainRetrofitService(HospitalService.class)
                .getStoreInfo(request);
    }

    @Override
    public Observable<GoodsListResponse> getGoodsList(GoodsListRequest request) {
        return mRepositoryManager.obtainRetrofitService(HospitalService.class)
                .getGoodsList(request);
    }
}