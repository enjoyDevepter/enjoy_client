package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.CashCoinContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.UserService;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.GetCashCoinRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.GetCashCoinResponse;


@ActivityScope
public class CashCoinModel extends BaseModel implements CashCoinContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public CashCoinModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

      @Override
      public Observable<GetCashCoinResponse> getCashCoin(GetCashCoinRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
	                .getCashCoin(request);
    }

}