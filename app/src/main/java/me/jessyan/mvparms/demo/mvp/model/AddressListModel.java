package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.AddressListContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.UserService;
import me.jessyan.mvparms.demo.mvp.model.entity.request.AddressListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DelAddressRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.AddressListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;


@ActivityScope
public class AddressListModel extends BaseModel implements AddressListContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public AddressListModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<BaseResponse> delAddress(DelAddressRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class).delAddrss(request);
    }

    @Override
    public Observable<AddressListResponse> getAddressList(AddressListRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class).getAddressList(request);
    }
}