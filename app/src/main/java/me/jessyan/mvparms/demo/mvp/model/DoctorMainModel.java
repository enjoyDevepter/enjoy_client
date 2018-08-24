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
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.CommentDoctorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.CommentDoctorResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorHotCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorHotCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LikeDoctorCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LikeDoctorCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LikeDoctorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LikeDoctorResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LoginUserDoctorHotCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LoginUserDoctorHotCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LoginUserDoctorInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LoginUserDoctorInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.UnLikeDoctorCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.UnLikeDoctorCommentResponse;
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
    public Observable<CommentDoctorResponse> commentDoctor(CommentDoctorRequest request) {
        return mRepositoryManager.obtainRetrofitService(DoctorService.class)
                .commentDoctor(request);
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

    @Override
    public Observable<LoginUserDoctorHotCommentResponse> loginUserRequestDoctorHotComment(LoginUserDoctorHotCommentRequest request) {
        return mRepositoryManager.obtainRetrofitService(DoctorService.class)
                .loginUserRequestHotComment(request);
    }

    @Override
    public Observable<DoctorHotCommentResponse> requestDoctorHotComment(DoctorHotCommentRequest request) {
        return mRepositoryManager.obtainRetrofitService(DoctorService.class)
                .requestHotComment(request);
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