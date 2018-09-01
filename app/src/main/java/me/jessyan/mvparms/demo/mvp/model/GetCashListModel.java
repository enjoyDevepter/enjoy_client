package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.GetCashListContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.UserService;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.GetCashListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.GetCashListResponse;


@ActivityScope
public class GetCashListModel extends BaseModel implements GetCashListContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public GetCashListModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<GetCashListResponse> getCashList(GetCashListRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .getCashList(request);
    }

}