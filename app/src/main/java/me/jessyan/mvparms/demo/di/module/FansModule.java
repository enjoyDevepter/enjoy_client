package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.FansContract;
import me.jessyan.mvparms.demo.mvp.model.FansModel;


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
}