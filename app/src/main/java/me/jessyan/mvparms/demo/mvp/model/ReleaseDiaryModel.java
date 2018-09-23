package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.ReleaseDiaryContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.DiaryService;
import me.jessyan.mvparms.demo.mvp.model.api.service.MainService;
import me.jessyan.mvparms.demo.mvp.model.entity.diary.ProjectRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.ReleaseDiaryRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DirayProjectListResponse;
import okhttp3.MultipartBody;


@ActivityScope
public class ReleaseDiaryModel extends BaseModel implements ReleaseDiaryContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public ReleaseDiaryModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<BaseResponse> uploadImage(String type, MultipartBody.Part imgs) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .uploadImage(type, imgs);
    }

    @Override
    public Observable<BaseResponse> releaseDiary(ReleaseDiaryRequest request) {
        return mRepositoryManager.obtainRetrofitService(MainService.class)
                .releaseDiary(request);
    }

    @Override
    public Observable<DirayProjectListResponse> getProjects(ProjectRequest request) {
        return mRepositoryManager.obtainRetrofitService(DiaryService.class)
                .getProjects(request);
    }
}