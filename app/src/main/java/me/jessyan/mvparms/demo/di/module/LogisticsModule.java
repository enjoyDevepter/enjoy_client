package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.LogisticsContract;
import me.jessyan.mvparms.demo.mvp.model.LogisticsModel;


@Module
public class LogisticsModule {
    private LogisticsContract.View view;

    /**
     * 构建LogisticsModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public LogisticsModule(LogisticsContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    LogisticsContract.View provideLogisticsView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    LogisticsContract.Model provideLogisticsModel(LogisticsModel model) {
        return model;
    }
}