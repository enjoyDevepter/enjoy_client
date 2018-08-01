package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.ForgetContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.LoginAndRegisterService;
import me.jessyan.mvparms.demo.mvp.model.entity.request.ForgetRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.VeritfyRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.RegisterResponse;


@ActivityScope
public class ForgetModel extends BaseModel implements ForgetContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public ForgetModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<BaseResponse> getVerifyForFind(VeritfyRequest veritfyRequest) {
        return mRepositoryManager.obtainRetrofitService(LoginAndRegisterService.class)
                .getVerifyForFind(veritfyRequest);
    }

    @Override
    public Observable<RegisterResponse> find(ForgetRequest forgetRequest) {
        return mRepositoryManager.obtainRetrofitService(LoginAndRegisterService.class)
                .find(forgetRequest);
    }
}