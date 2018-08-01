package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.ForgetContract;
import me.jessyan.mvparms.demo.mvp.model.ForgetModel;


@Module
public class ForgetModule {
    private ForgetContract.View view;

    /**
     * 构建ForgetModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ForgetModule(ForgetContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ForgetContract.View provideForgetView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ForgetContract.Model provideForgetModel(ForgetModel model) {
        return model;
    }
}