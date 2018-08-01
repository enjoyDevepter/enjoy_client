package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.ModifyContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.LoginAndRegisterService;
import me.jessyan.mvparms.demo.mvp.model.entity.request.ModifyRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;


@ActivityScope
public class ModifyModel extends BaseModel implements ModifyContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public ModifyModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<BaseResponse> modify(ModifyRequest request) {
        return mRepositoryManager.obtainRetrofitService(LoginAndRegisterService.class)
                .modify(request);
    }
}