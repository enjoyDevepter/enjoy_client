package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.DoctorMainContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.DoctorService;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LikeDoctorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LikeDoctorResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LoginUserDoctorInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LoginUserDoctorInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.UnLikeDoctorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.UnLikeDoctorResponse;


@ActivityScope
public class DoctorMainModel extends BaseModel implements DoctorMainContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public DoctorMainModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<DoctorInfoResponse> requestDoctorInfo(DoctorInfoRequest request) {
        return mRepositoryManager.obtainRetrofitService(DoctorService.class)
                .requestDoctorInfo(request);
    }

    @Override
    public Observable<LoginUserDoctorInfoResponse> requestLoginUserDoctorInfo(LoginUserDoctorInfoRequest request) {
        return mRepositoryManager.obtainRetrofitService(DoctorService.class)
                .requestLoginUserDoctorInfo(request);
    }

    @Override
    public Observable<LikeDoctorResponse> likeDoctor(LikeDoctorRequest request) {
        return mRepositoryManager.obtainRetrofitService(DoctorService.class)
                .likeDoctor(request);
    }

    @Override
    public Observable<UnLikeDoctorResponse> unlikeDoctor(UnLikeDoctorRequest request) {
        return mRepositoryManager.obtainRetrofitService(DoctorService.class)
                .unlikeDoctor(request);
    }
}