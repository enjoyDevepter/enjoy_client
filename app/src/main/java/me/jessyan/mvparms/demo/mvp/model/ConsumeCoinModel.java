package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.ConsumeCoinContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.UserService;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.GetConsumeInfoPageRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.GetConsumeInfoPageResponse;


@ActivityScope
public class ConsumeCoinModel extends BaseModel implements ConsumeCoinContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public ConsumeCoinModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

     @Override
    public Observable<GetConsumeInfoPageResponse> getconsumeInfoPage(GetConsumeInfoPageRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
	                .getConsumeInfoPage(request);
    }

}