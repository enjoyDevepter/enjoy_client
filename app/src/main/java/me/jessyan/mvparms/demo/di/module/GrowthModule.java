package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.GrowthContract;
import me.jessyan.mvparms.demo.mvp.model.GrowthModel;


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
}