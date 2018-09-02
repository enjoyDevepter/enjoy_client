package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.UserInfoContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.MainService;
import me.jessyan.mvparms.demo.mvp.model.api.service.UserService;
import me.jessyan.mvparms.demo.mvp.model.entity.request.SimpleRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.AllAddressResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.ModifyUserInfoRequest;
import okhttp3.MultipartBody;


@ActivityScope
public class UserInfoModel extends BaseModel implements UserInfoContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public UserInfoModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<BaseResponse> uploadImage(String type, MultipartBody.Part imgs) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .uploadImage(type, imgs);
    }

    @Override
    public Observable<BaseResponse> modifyUserInfo(ModifyUserInfoRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .modifyUserInfo(request);
    }

    @Override
    public Observable<AllAddressResponse> getAllAddressList(SimpleRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .getAllAddressList(request);
    }
}