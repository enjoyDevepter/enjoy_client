package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.PlatformContract;
import me.jessyan.mvparms.demo.mvp.model.PlatformModel;


@Module
public class PlatformModule {
    private PlatformContract.View view;

    /**
     * 构建PlatformModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public PlatformModule(PlatformContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    PlatformContract.View providePlatformView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    PlatformContract.Model providePlatformModel(PlatformModel model) {
        return model;
    }
}