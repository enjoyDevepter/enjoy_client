package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.InviteListContract;
import me.jessyan.mvparms.demo.mvp.model.InviteListModel;


@Module
public class InviteListModule {
    private InviteListContract.View view;

    /**
     * 构建InviteListModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public InviteListModule(InviteListContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    InviteListContract.View provideInviteListView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    InviteListContract.Model provideInviteListModel(InviteListModel model) {
        return model;
    }
}