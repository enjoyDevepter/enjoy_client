package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.NewlywedsContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.MainService;
import me.jessyan.mvparms.demo.mvp.model.entity.request.GoodsListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.GoodsListResponse;


@ActivityScope
public class NewlywedsModel extends BaseModel implements NewlywedsContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public NewlywedsModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<GoodsListResponse> getNewlywedsGoodsList(GoodsListRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .getGoodsList(request);
    }
}