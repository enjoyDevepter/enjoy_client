package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.ActivityInfoContract;
import me.jessyan.mvparms.demo.mvp.model.ActivityInfoModel;


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
}