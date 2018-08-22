package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.DoctorPaperContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.DoctorService;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorIntorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorIntorResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorPaperRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorPaperResponse;


@ActivityScope
public class DoctorPaperModel extends BaseModel implements DoctorPaperContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public DoctorPaperModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<DoctorPaperResponse> getDoctorPaper(DoctorPaperRequest request) {
        return mRepositoryManager.obtainRetrofitService(DoctorService.class)
                .getDoctorPaper(request);
    }
}