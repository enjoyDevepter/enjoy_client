package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.UserIntegralContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.ScoreService;
import me.jessyan.mvparms.demo.mvp.model.api.service.UserService;
import me.jessyan.mvparms.demo.mvp.model.entity.score.UserScorePageRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.score.UserScorePageResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.QiandaoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.QiandaoResponse;


@ActivityScope
public class UserIntegralModel extends BaseModel implements UserIntegralContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public UserIntegralModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<UserScorePageResponse> requestScorePage(UserScorePageRequest request) {
        return mRepositoryManager.obtainRetrofitService(ScoreService.class)
                .requestScorePage(request);
    }

    @Override
    public Observable<QiandaoResponse> qiandao(QiandaoRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .qiandao(request);
    }
}