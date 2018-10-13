package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.ActivityInfoContract;
import me.jessyan.mvparms.demo.mvp.model.ActivityInfoModel;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean.ActivityInfo;
import me.jessyan.mvparms.demo.mvp.ui.adapter.ActivityListAdapter;


@Module
public class ActivityInfoModule {
    private ActivityInfoContract.View view;

    /**
     * 构建ActivityInfoModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ActivityInfoModule(ActivityInfoContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ActivityInfoContract.View provideActivityInfoView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ActivityInfoContract.Model provideActivityInfoModel(ActivityInfoModel model) {
        return model;
    }


    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @ActivityScope
    @Provides
    List<ActivityInfo> provideActivityList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    ActivityListAdapter provideActivityListAdapter(List<ActivityInfo> goods) {
        return new ActivityListAdapter(goods);
    }
}