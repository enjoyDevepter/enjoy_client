package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.ModifyPasswordContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.UserService;
import me.jessyan.mvparms.demo.mvp.model.entity.response.RegisterResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.ModifyUserInfoRequest;


@ActivityScope
public class ModifyPasswordModel extends BaseModel implements ModifyPasswordContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public ModifyPasswordModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<RegisterResponse> modifyPassword(ModifyUserInfoRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .modifyPasswordInfo(request);
    }
}