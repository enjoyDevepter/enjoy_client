package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.DoctorAllCommentContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.DoctorService;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.DoctorAllCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.DoctorAllCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.LikeDoctorCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.LikeDoctorCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.LoginUserDoctorAllCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.LoginUserDoctorAllCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.UnLikeDoctorCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.UnLikeDoctorCommentResponse;


@ActivityScope
public class DoctorAllCommentModel extends BaseModel implements DoctorAllCommentContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public DoctorAllCommentModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<DoctorAllCommentResponse> requestAllComment(DoctorAllCommentRequest request) {
        return mRepositoryManager.obtainRetrofitService(DoctorService.class)
	                .requestAllComment(request);
    }

    @Override
    public Observable<LoginUserDoctorAllCommentResponse> loginUserRequestAllComment(LoginUserDoctorAllCommentRequest request) {
        return mRepositoryManager.obtainRetrofitService(DoctorService.class)
                .loginUserRequestAllComment(request);
    }

    @Override
    public Observable<LikeDoctorCommentResponse> likeDoctorComment(LikeDoctorCommentRequest request) {
        return mRepositoryManager.obtainRetrofitService(DoctorService.class)
                .likeDoctorComment(request);
    }

    @Override
    public Observable<UnLikeDoctorCommentResponse> unLikeDoctorComment(UnLikeDoctorCommentRequest request) {
        return mRepositoryManager.obtainRetrofitService(DoctorService.class)
                .unLikeDoctorComment(request);
    }

}