package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.HomeContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.MainService;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryVoteRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.FollowMemberRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.HomeRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HomeResponse;


@ActivityScope
public class HomeModel extends BaseModel implements HomeContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public HomeModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<HomeResponse> getHomeInfo(HomeRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .getHomeInfo(request);
    }

    @Override
    public Observable<DiaryListResponse> getDiaryList(DiaryListRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .getDiaryList(request);
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