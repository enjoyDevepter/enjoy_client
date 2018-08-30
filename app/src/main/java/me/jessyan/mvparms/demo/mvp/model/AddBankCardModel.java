package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.AddBankCardContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.UserService;
import me.jessyan.mvparms.demo.mvp.model.entity.request.SimpleRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.AllAddressResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.AddBankCardRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.BankListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.AddBankCardResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.BankListResponse;


@ActivityScope
public class AddBankCardModel extends BaseModel implements AddBankCardContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public AddBankCardModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<AddBankCardResponse> addBankCard(AddBankCardRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .addBankCard(request);
    }

    @Override
    public Observable<BankListResponse> getBankList(BankListRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .getBackList(request);
    }

}