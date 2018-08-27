package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.ModifyUserInfoContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.UserService;
import me.jessyan.mvparms.demo.mvp.model.entity.request.SimpleRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.ModifyUserInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.CommonUserInfoResponse;


@ActivityScope
public class ModifyUserInfoModel extends BaseModel implements ModifyUserInfoContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public ModifyUserInfoModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<BaseResponse> modifyUserInfo(ModifyUserInfoRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .modifyUserInfo(request);
    }

    @Override
    public Observable<CommonUserInfoResponse> getCommonUserInfo(SimpleRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .getCommonUserInfo(request);
    }
}