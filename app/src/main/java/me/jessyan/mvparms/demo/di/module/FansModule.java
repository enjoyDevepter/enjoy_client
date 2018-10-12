package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.FansContract;
import me.jessyan.mvparms.demo.mvp.model.FansModel;
import me.jessyan.mvparms.demo.mvp.model.entity.Member;
import me.jessyan.mvparms.demo.mvp.ui.adapter.FollowMemberAdapter;


@Module
public class FansModule {
    private FansContract.View view;

    /**
     * 构建FansModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public FansModule(FansContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    FansContract.View provideFansView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    FansContract.Model provideFansModel(FansModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    FollowMemberAdapter provideStoreAdapter(List<Member> list) {
        return new FollowMemberAdapter(list);
    }


    @ActivityScope
    @Provides
    List<Member> provideOrderBeanList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

}