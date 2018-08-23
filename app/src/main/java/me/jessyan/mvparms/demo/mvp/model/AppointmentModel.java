package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.AppointmentContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.AppointmentService;
import me.jessyan.mvparms.demo.mvp.model.entity.request.AppointmentAndMealRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.AppointmentResponse;


@ActivityScope
public class AppointmentModel extends BaseModel implements AppointmentContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public AppointmentModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<AppointmentResponse> getAppointment(AppointmentAndMealRequest request) {
        return mRepositoryManager.obtainRetrofitService(AppointmentService.class)
                .getAppointment(request);
    }
}