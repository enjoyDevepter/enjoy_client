package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.DiscoverContract;
import me.jessyan.mvparms.demo.mvp.model.DiscoverModel;


@Module
public class DiscoverModule {
    private DiscoverContract.View view;

    /**
     * 构建DiscoverModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public DiscoverModule(DiscoverContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    DiscoverContract.View provideDiscoverView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    DiscoverContract.Model provideDiscoverModel(DiscoverModel model) {
        return model;
    }
}