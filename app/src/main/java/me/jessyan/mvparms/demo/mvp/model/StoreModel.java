package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.StoreContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.HospitalService;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.request.HospitalListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.response.HospitalResponse;


@ActivityScope
public class StoreModel extends BaseModel implements StoreContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public StoreModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<HospitalResponse> getStroeList(HospitalListRequest request) {
        return mRepositoryManager.obtainRetrofitService(HospitalService.class)
                .getHospitalList(request);
    }
}