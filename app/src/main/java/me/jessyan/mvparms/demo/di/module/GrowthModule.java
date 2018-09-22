package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.GrowthContract;
import me.jessyan.mvparms.demo.mvp.model.GrowthModel;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.GrowthInfo;
import me.jessyan.mvparms.demo.mvp.ui.adapter.UserGrowthAdapter;


@Module
public class GrowthModule {
    private GrowthContract.View view;

    /**
     * 构建GrowthModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public GrowthModule(GrowthContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    GrowthContract.View provideGrowthView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    GrowthContract.Model provideGrowthModel(GrowthModel model) {
        return model;
    }


    @ActivityScope
    @Provides
    RecyclerView.Adapter provideStoreAdapter(List<GrowthInfo> list) {
        return new UserGrowthAdapter(list);
    }

    @ActivityScope
    @Provides
    List<GrowthInfo> provideOrderBeanList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }
}