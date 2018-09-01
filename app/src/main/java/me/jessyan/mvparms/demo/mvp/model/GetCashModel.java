package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.GetCashContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.MainService;
import me.jessyan.mvparms.demo.mvp.model.api.service.UserService;
import me.jessyan.mvparms.demo.mvp.model.entity.request.CollectGoodsRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.GetCashRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.UserInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.GetCashResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.UserInfoResponse;


@ActivityScope
public class GetCashModel extends BaseModel implements GetCashContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public GetCashModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<GetCashResponse> getCash(GetCashRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .getCash(request);
    }

    @Override
    public Observable<UserInfoResponse> getUserInfo(UserInfoRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .getUserInfo(request);
    }
}