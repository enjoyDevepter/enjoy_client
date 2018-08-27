package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import me.jessyan.mvparms.demo.mvp.contract.FeedBackContract;


@ActivityScope
public class FeedBackModel extends BaseModel implements FeedBackContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public FeedBackModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }
}