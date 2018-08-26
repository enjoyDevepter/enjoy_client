package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.CashConvertContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.UserService;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.CashConvertRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.GetCashCoinRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.UserInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.CashConvertResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.GetCashCoinResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.UserInfoResponse;


@ActivityScope
public class CashConvertModel extends BaseModel implements CashConvertContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public CashConvertModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<CashConvertResponse> convertCash(CashConvertRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .convertCash(request);
    }

    @Override
    public Observable<UserInfoResponse> getUserInfo(UserInfoRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .getUserInfo(request);
    }
}