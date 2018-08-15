package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.HGoodsDetailsContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.HGoodsService;
import me.jessyan.mvparms.demo.mvp.model.entity.request.GoodsDetailsRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HGoodsDetailsResponse;


@ActivityScope
public class HGoodsDetailsModel extends BaseModel implements HGoodsDetailsContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public HGoodsDetailsModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<HGoodsDetailsResponse> getHGoodsDetails(GoodsDetailsRequest request) {
        return mRepositoryManager.obtainRetrofitService(HGoodsService.class)
                .getHGoodsDetails(request);
    }
}