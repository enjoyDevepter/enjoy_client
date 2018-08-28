package me.jessyan.mvparms.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.contract.InviteMainContract;
import me.jessyan.mvparms.demo.mvp.model.api.service.UserService;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.FollowRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.GetMyMemberListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.GetMyMemberListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.ShareResponse;


@ActivityScope
public class InviteMainModel extends BaseModel implements InviteMainContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public InviteMainModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }


    @Override
    public Observable<GetMyMemberListResponse> getMyMemberList(GetMyMemberListRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .getMyMemberList(request);
    }

    @Override
    public Observable<ShareResponse> share(FollowRequest request) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .share(request);
    }
}