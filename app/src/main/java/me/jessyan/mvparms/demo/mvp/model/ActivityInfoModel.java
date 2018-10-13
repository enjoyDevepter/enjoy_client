package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.ActivityInfoContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.HospitalService;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.request.ActivityInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.response.ActivityInfoListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.response.ActivityInfoResponse;


@ActivityScope
public class ActivityInfoModel extends BaseModel implements ActivityInfoContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public ActivityInfoModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<ActivityInfoResponse> getActivityInfo(ActivityInfoRequest request) {
        return mRepositoryManager.obtainRetrofitService(HospitalService.class)
                .getActivityInfo(request);
    }

    @Override
    public Observable<ActivityInfoListResponse> getActivityList(ActivityInfoRequest request) {
        return mRepositoryManager.obtainRetrofitService(HospitalService.class)
                .getActivityList(request);
    }
}