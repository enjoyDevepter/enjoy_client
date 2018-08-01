package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.LoginContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.LoginAndRegisterService;
import me.jessyan.mvparms.demo.mvp.model.entity.request.LoginByPhoneRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.LoginByUserRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.VeritfyRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.RegisterResponse;


@ActivityScope
public class LoginModel extends BaseModel implements LoginContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public LoginModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<BaseResponse> getVerifyForUser(VeritfyRequest veritfyRequest) {
        return mRepositoryManager.obtainRetrofitService(LoginAndRegisterService.class)
                .getVerifyForUser(veritfyRequest);
    }

    @Override
    public Observable<RegisterResponse> loginByPhone(LoginByPhoneRequest loginByPhoneRequest) {
        return mRepositoryManager.obtainRetrofitService(LoginAndRegisterService.class)
                .loginByPhone(loginByPhoneRequest);
    }

    @Override
    public Observable<RegisterResponse> loginByUserName(LoginByUserRequest loginByUserRequest) {
        return mRepositoryManager.obtainRetrofitService(LoginAndRegisterService.class)
                .loginByUser(loginByUserRequest);
    }
}