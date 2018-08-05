package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.SelfPickupAddrListContract;
import me.jessyan.mvparms.demo.mvp.model.SelfPickupAddrListModel;


@Module
public class SelfPickupAddrListModule {
    private SelfPickupAddrListContract.View view;

    /**
     * 构建SelfPickupAddrListModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public SelfPickupAddrListModule(SelfPickupAddrListContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    SelfPickupAddrListContract.View provideSelfPickupAddrListView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    SelfPickupAddrListContract.Model provideSelfPickupAddrListModel(SelfPickupAddrListModel model) {
        return model;
    }
}