package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.DoctorHonorContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.DoctorService;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.DoctorHonorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.DoctorHonorResponse;


@ActivityScope
public class DoctorHonorModel extends BaseModel implements DoctorHonorContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public DoctorHonorModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }


    @Override
    public Observable<DoctorHonorResponse> getDoctorHonor(DoctorHonorRequest request) {
        return mRepositoryManager.obtainRetrofitService(DoctorService.class)
                .getDoctorHonor(request);
    }
}