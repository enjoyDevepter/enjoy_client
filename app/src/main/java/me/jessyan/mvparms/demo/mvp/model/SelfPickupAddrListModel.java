package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.SelfPickupAddrListContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.MainService;
import me.jessyan.mvparms.demo.mvp.model.api.service.UserService;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.request.HospitalListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.response.HospitalListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.request.SimpleRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.StoresListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.AllAddressResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.StoresListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.RelatedStoreRequest;


@ActivityScope
public class SelfPickupAddrListModel extends BaseModel implements SelfPickupAddrListContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public SelfPickupAddrListModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<AllAddressResponse> getAllAddressList(SimpleRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .getAllAddressList(request);
    }

    @Override
    public Observable<StoresListResponse> getStores(StoresListRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .getStores(request);
    }

    @Override
    public Observable<HospitalListResponse> getHospitals(HospitalListRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .getHospitals(request);
    }

    @Override
    public Observable<BaseResponse> relateStore(RelatedStoreRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .relateStore(request);
    }
}