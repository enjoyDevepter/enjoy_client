package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.MyMealContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.AppointmentService;
import me.jessyan.mvparms.demo.mvp.model.api.service.MainService;
import me.jessyan.mvparms.demo.mvp.model.entity.order.request.OrderOperationRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.AppointmentAndMealRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.ModifyAppointmentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.MyMealResponse;


@ActivityScope
public class MyMealModel extends BaseModel implements MyMealContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public MyMealModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<MyMealResponse> getMyMealAppointment(AppointmentAndMealRequest request) {
        return mRepositoryManager.obtainRetrofitService(AppointmentService.class)
                .getMyMealAppointment(request);
    }

    @Override
    public Observable<BaseResponse> modifyAppointmentTime(ModifyAppointmentRequest request) {
        return mRepositoryManager.obtainRetrofitService(AppointmentService.class)
                .modifyAppointmentTime(request);
    }

    @Override
    public Observable<BaseResponse> cancelOrder(OrderOperationRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .cancelOrder(request);
    }
}