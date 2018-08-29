package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.ChoiceProjectForDiaryContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.DiaryService;
import me.jessyan.mvparms.demo.mvp.model.entity.diary.ProjectRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.GoodsListResponse;


@ActivityScope
public class ChoiceProjectForDiaryModel extends BaseModel implements ChoiceProjectForDiaryContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public ChoiceProjectForDiaryModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<GoodsListResponse> getProjects(ProjectRequest request) {
        return mRepositoryManager.obtainRetrofitService(DiaryService.class)
                .getProjects(request);
    }
}