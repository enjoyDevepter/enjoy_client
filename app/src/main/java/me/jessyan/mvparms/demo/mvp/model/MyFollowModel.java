package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.MyFollowContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.UserService;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.FollowRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.MyFollowRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.MyFollowListResponse;


@ActivityScope
public class MyFollowModel extends BaseModel implements MyFollowContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public MyFollowModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<MyFollowListResponse> getMyFollows(MyFollowRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .getMyFollows(request);
    }

    @Override
    public Observable<BaseResponse> follow(FollowRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .follow(request);
    }
}