package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.HospitalInfoContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.DoctorService;
import me.jessyan.mvparms.demo.mvp.model.api.service.HospitalService;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.request.HospitalInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.request.LoginUserHospitalInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.response.HospitalInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.response.LoginUserHospitalInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorListResponse;


@ActivityScope
public class HospitalInfoModel extends BaseModel implements HospitalInfoContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public HospitalInfoModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }


    @Override
    public Observable<LoginUserHospitalInfoResponse> requestHospitalByUser(LoginUserHospitalInfoRequest request) {
        return mRepositoryManager.obtainRetrofitService(HospitalService.class)
                .requestHospitalInfoForLoginUser(request);
    }


    @Override
    public Observable<HospitalInfoResponse> requestHospital(HospitalInfoRequest request) {
        return mRepositoryManager.obtainRetrofitService(HospitalService.class)
                .requestHospitalInfo(request);
    }

    @Override
    public Observable<DoctorListResponse> requestDoctorPage(DoctorListRequest request) {
        return mRepositoryManager.obtainRetrofitService(DoctorService.class)
                .requestDoctorPage(request);
    }

}