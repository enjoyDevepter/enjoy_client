package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.PickCouponContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.UserService;
import me.jessyan.mvparms.demo.mvp.model.entity.request.PickCouponRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.PickCouponListResponse;


@ActivityScope
public class PickCouponModel extends BaseModel implements PickCouponContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public PickCouponModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<PickCouponListResponse> getPickCouponList(PickCouponRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .getPickCouponList(request);
    }

    @Override
    public Observable<BaseResponse> pickCoupon(PickCouponRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .pickCoupon(request);
    }
}