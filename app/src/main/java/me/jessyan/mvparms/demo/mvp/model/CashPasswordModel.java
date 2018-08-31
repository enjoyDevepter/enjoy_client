package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.CashPasswordContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.LoginAndRegisterService;
import me.jessyan.mvparms.demo.mvp.model.api.service.UserService;
import me.jessyan.mvparms.demo.mvp.model.entity.request.VeritfyRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.SetCashPasswordRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.UserInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.SetCashPasswordResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.UserInfoResponse;


@ActivityScope
public class CashPasswordModel extends BaseModel implements CashPasswordContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public CashPasswordModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<SetCashPasswordResponse> setCashPassword(SetCashPasswordRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .setCashPassword(request);
    }

    @Override
    public Observable<BaseResponse> getVerifyForFind(VeritfyRequest veritfyRequest) {
        return mRepositoryManager.obtainRetrofitService(LoginAndRegisterService.class)
                .getVerifyForFind(veritfyRequest);
    }
}