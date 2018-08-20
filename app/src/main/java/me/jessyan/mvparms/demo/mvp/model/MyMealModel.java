package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.MyMealContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.AppointmentService;
import me.jessyan.mvparms.demo.mvp.model.entity.request.AppointmentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.AppointmentResponse;


@ActivityScope
public class MyMealModel extends BaseModel implements MyMealContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public MyMealModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<AppointmentResponse> getMyMealAppointment(AppointmentRequest request) {
        return mRepositoryManager.obtainRetrofitService(AppointmentService.class)
                .getMyMealAppointment(request);
    }
}