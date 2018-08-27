package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.AuthenticationContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.MainService;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.AuthenticationRequest;
import okhttp3.MultipartBody;


@ActivityScope
public class AuthenticationModel extends BaseModel implements AuthenticationContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public AuthenticationModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<BaseResponse> uploadImage(MultipartBody.Part imgs) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .uploadImage(imgs);
    }

    @Override
    public Observable<BaseResponse> auth(AuthenticationRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .auth(request);
    }
}