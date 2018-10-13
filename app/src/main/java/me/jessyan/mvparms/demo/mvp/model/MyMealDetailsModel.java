package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.MyMealDetailsContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.AppointmentService;
import me.jessyan.mvparms.demo.mvp.model.api.service.MainService;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.ModifyAppointmentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.MyMealDetailRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.ShareRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.AppointmentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryApplyResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.ShareResponse;


@ActivityScope
public class MyMealDetailsModel extends BaseModel implements MyMealDetailsContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public MyMealDetailsModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<AppointmentResponse> getAppointment(MyMealDetailRequest request) {
        return mRepositoryManager.obtainRetrofitService(AppointmentService.class)
                .getAppointment(request);
    }

    @Override
    public Observable<BaseResponse> cancelAppointment(ModifyAppointmentRequest request) {
        return mRepositoryManager.obtainRetrofitService(AppointmentService.class)
                .cancelAppointment(request);
    }

    @Override
    public Observable<ShareResponse> share(ShareRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .share(request);
    }

    @Override
    public Observable<DiaryApplyResponse> apply(DiaryRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .apply(request);
    }
}