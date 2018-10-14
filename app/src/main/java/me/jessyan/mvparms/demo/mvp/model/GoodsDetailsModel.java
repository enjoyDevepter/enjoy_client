package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.GoodsDetailsContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.MainService;
import me.jessyan.mvparms.demo.mvp.model.entity.request.AddGoodsToCartRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.CollectGoodsRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryForGoodsRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.GoodsDetailsRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.SimpleRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.GoodsDetailsResponse;


@ActivityScope
public class GoodsDetailsModel extends BaseModel implements GoodsDetailsContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public GoodsDetailsModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<GoodsDetailsResponse> getGoodsDetails(GoodsDetailsRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .getGoodsDetails(request);
    }

    @Override
    public Observable<BaseResponse> addGoodsToCart(AddGoodsToCartRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .addGoodsToCart(request);
    }

    @Override
    public Observable<BaseResponse> collectGoods(CollectGoodsRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .collectGoods(request);
    }

    @Override
    public Observable<DiaryListResponse> getDiaryForGoodsIdList(DiaryForGoodsRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .getDiaryForGoodsIdList(request);
    }

    @Override
    public Observable<BaseResponse> getTel(SimpleRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .getTel(request);
    }
}