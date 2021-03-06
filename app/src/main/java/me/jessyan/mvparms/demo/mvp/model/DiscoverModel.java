package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.DiscoverContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.MainService;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryVoteRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.FollowMemberRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.SimpleRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryNaviListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryTypeListResponse;


@ActivityScope
public class DiscoverModel extends BaseModel implements DiscoverContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public DiscoverModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<DiaryTypeListResponse> getDiaryType(SimpleRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .getDiaryType(request);
    }

    @Override
    public Observable<DiaryNaviListResponse> getDiaryNaviType(SimpleRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .getDiaryNaviType(request);
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