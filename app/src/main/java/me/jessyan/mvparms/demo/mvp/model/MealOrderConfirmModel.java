package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.MealOrderConfirmContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.MainService;
import me.jessyan.mvparms.demo.mvp.model.entity.request.MealOrderConfrimRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.PayMealOrderRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.MealOrderConfirmResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.PayMealOrderResponse;


@ActivityScope
public class MealOrderConfirmModel extends BaseModel implements MealOrderConfirmContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public MealOrderConfirmModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<MealOrderConfirmResponse> getMealOrderConfirmInfo(MealOrderConfrimRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .getMealOrderConfirmInfo(request);
    }

    @Override
    public Observable<PayMealOrderResponse> placeMealOrder(PayMealOrderRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .placeMealOrder(request);
    }
}