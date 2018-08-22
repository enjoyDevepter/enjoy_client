package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.ChoiceTimeContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.AppointmentService;
import me.jessyan.mvparms.demo.mvp.model.entity.request.GetAppointmentTimeRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.ModifyAppointmentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.GetAppointmentTimeResponse;


@ActivityScope
public class ChoiceTimeModel extends BaseModel implements ChoiceTimeContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public ChoiceTimeModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<GetAppointmentTimeResponse> getAppointmentTime(GetAppointmentTimeRequest request) {
        return mRepositoryManager.obtainRetrofitService(AppointmentService.class)
                .getAppointmentTime(request);
    }

    @Override
    public Observable<BaseResponse> modifyAppointmentTime(ModifyAppointmentRequest request) {
        return mRepositoryManager.obtainRetrofitService(AppointmentService.class)
                .modifyAppointmentTime(request);
    }
}