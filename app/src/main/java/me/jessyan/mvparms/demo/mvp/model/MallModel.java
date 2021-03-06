package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.MallContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.HGoodsService;
import me.jessyan.mvparms.demo.mvp.model.api.service.MainService;
import me.jessyan.mvparms.demo.mvp.model.entity.request.GoodsListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.SimpleRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.CategoryResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.GoodsListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HGoodsListResponse;


@ActivityScope
public class MallModel extends BaseModel implements MallContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public MallModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<CategoryResponse> getCategory(SimpleRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .getCategory(request);
    }


    @Override
    public Observable<GoodsListResponse> getGoodsList(GoodsListRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .getGoodsList(request);
    }

    @Override
    public Observable<HGoodsListResponse> getHGoodsList(GoodsListRequest request) {
        return mRepositoryManager.obtainRetrofitService(HGoodsService.class)
                .getHGoodsList(request);
    }

}