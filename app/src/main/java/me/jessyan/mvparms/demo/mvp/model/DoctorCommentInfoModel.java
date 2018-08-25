package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.DoctorCommentInfoContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.DoctorService;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.GetDoctorCommentReplyPageRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.LikeDoctorCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.ReplyDoctorCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.UnLikeDoctorCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.GetDoctorCommentReplyPageResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.LikeDoctorCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.ReplyDoctorCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.UnLikeDoctorCommentResponse;


@ActivityScope
public class DoctorCommentInfoModel extends BaseModel implements DoctorCommentInfoContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public DoctorCommentInfoModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

     @Override
     public Observable<GetDoctorCommentReplyPageResponse> getDoctorCommentReplyPage(GetDoctorCommentReplyPageRequest request) {
        return mRepositoryManager.obtainRetrofitService(DoctorService.class)
	                .getDoctorCommentReply(request);
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

    @Override
    public Observable<ReplyDoctorCommentResponse> replyDoctorComment(ReplyDoctorCommentRequest request) {
        return mRepositoryManager.obtainRetrofitService(DoctorService.class)
                .replyDoctorComment(request);
    }
}