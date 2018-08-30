package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.FeedBackContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.LoginAndRegisterService;
import me.jessyan.mvparms.demo.mvp.model.api.service.UserService;
import me.jessyan.mvparms.demo.mvp.model.entity.request.ForgetRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.RegisterResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.FeedbackRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.FeedbackResponse;


@ActivityScope
public class FeedBackModel extends BaseModel implements FeedBackContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public FeedBackModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<FeedbackResponse> feedback(FeedbackRequest forgetRequest) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .feedback(forgetRequest);
    }
}