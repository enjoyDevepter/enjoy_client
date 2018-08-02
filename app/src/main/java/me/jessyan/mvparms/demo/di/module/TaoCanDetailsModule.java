package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.TaoCanDetailsContract;
import me.jessyan.mvparms.demo.mvp.model.TaoCanDetailsModel;


@Module
public class TaoCanDetailsModule {
    private TaoCanDetailsContract.View view;

    /**
     * 构建TaoCanDetailsModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public TaoCanDetailsModule(TaoCanDetailsContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    TaoCanDetailsContract.View provideTaoCanDetailsView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    TaoCanDetailsContract.Model provideTaoCanDetailsModel(TaoCanDetailsModel model) {
        return model;
    }
}