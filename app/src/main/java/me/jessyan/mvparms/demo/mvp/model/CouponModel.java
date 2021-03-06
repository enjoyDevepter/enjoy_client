package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.CouponContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.UserService;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.MyCouponListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.MyCouponListResponse;


@ActivityScope
public class CouponModel extends BaseModel implements CouponContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public CouponModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<MyCouponListResponse> getMyCouponList(MyCouponListRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .getMyCouponList(request);
    }
}