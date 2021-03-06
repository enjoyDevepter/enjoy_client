package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.GetCashContract;
import me.jessyan.mvparms.demo.mvp.model.GetCashModel;


@Module
public class GetCashModule {
    private GetCashContract.View view;

    /**
     * 构建GetCashModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public GetCashModule(GetCashContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    GetCashContract.View provideGetCashView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    GetCashContract.Model provideGetCashModel(GetCashModel model) {
        return model;
    }
}