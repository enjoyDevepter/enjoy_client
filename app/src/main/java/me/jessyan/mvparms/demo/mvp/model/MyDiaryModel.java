package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.MyDiaryContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.MainService;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryVoteRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.FollowMemberRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryApplyResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryListResponse;


@ActivityScope
public class MyDiaryModel extends BaseModel implements MyDiaryContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public MyDiaryModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<DiaryListResponse> getMyDiaryList(DiaryListRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .getDiaryList(request);
    }

    @Override
    public Observable<DiaryApplyResponse> apply(DiaryRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .apply(request);
    }

    @Override
    public Observable<BaseResponse> diaryVote(DiaryVoteRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .diaryVote(request);
    }

    @Override
    public Observable<BaseResponse> follow(FollowMemberRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .follow(request);
    }
}