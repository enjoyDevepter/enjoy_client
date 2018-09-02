package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.ConsumeCoinInputContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.UserService;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.GetConsumeInfoPageRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.GetRechargeListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.GetConsumeInfoPageResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.GetRechargeListResponse;


@ActivityScope
public class ConsumeCoinInputModel extends BaseModel implements ConsumeCoinInputContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public ConsumeCoinInputModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }


    @Override
    public Observable<GetRechargeListResponse> getRechargeList(GetRechargeListRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .getRechargeList(request);
    }

}