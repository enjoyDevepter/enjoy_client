package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.GetCashListContract;
import me.jessyan.mvparms.demo.mvp.model.GetCashListModel;


@Module
public class GetCashListModule {
    private GetCashListContract.View view;

    /**
     * 构建GetCashListModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public GetCashListModule(GetCashListContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    GetCashListContract.View provideGetCashListView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    GetCashListContract.Model provideGetCashListModel(GetCashListModel model) {
        return model;
    }
}