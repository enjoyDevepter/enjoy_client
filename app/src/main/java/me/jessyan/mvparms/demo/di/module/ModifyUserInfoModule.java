package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.ModifyUserInfoContract;
import me.jessyan.mvparms.demo.mvp.model.ModifyUserInfoModel;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.CommonUserInfo;
import me.jessyan.mvparms.demo.mvp.ui.adapter.CommonUserInfoAdapter;


@Module
public class ModifyUserInfoModule {
    private ModifyUserInfoContract.View view;

    /**
     * 构建ModifyUserInfoModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ModifyUserInfoModule(ModifyUserInfoContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ModifyUserInfoContract.View provideModifyUserInfoView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ModifyUserInfoContract.Model provideModifyUserInfoModel(ModifyUserInfoModel model) {
        return model;
    }


    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @ActivityScope
    @Provides
    List<CommonUserInfo> provideDiariesList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    CommonUserInfoAdapter provideGoodsListAdapter(List<CommonUserInfo> diaries) {
        return new CommonUserInfoAdapter(diaries);
    }
}