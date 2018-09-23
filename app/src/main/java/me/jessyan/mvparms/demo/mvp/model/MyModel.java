package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.MyContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.UserService;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.UserInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.MyDiaryInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.UserInfoResponse;


@ActivityScope
public class MyModel extends BaseModel implements MyContract.Model {
    /**
     * 保存用户信息的key
     */
    public static final String KEY_FOR_USER_INFO = "KEY_FOR_USER_INFO";
    /**
     * 保存用户账户的key
     */
    public static final String KEY_FOR_USER_ACCOUNT = "KEY_FOR_USER_ACCOUNT";
    private Gson mGson;
    private Application mApplication;


    @Inject
    public MyModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<UserInfoResponse> getUserInfo(UserInfoRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .getUserInfo(request);
    }

    @Override
    public Observable<MyDiaryInfoResponse> getMyDiaryInfo(UserInfoRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .getMyDiaryInfo(request);
    }

}