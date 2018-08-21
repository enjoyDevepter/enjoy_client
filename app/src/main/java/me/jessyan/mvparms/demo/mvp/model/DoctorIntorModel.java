package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.DoctorIntorContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.DoctorService;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorIntorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorIntorResponse;


@ActivityScope
public class DoctorIntorModel extends BaseModel implements DoctorIntorContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public DoctorIntorModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<DoctorIntorResponse> getDoctorIntor(DoctorIntorRequest request) {
        return mRepositoryManager.obtainRetrofitService(DoctorService.class)
                .getDoctorIntor(request);
    }
}