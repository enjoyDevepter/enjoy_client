package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.InviteMainContract;
import me.jessyan.mvparms.demo.mvp.model.InviteMainModel;


@Module
public class InviteMainModule {
    private InviteMainContract.View view;

    /**
     * 构建InviteMainModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public InviteMainModule(InviteMainContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    InviteMainContract.View provideInviteMainView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    InviteMainContract.Model provideInviteMainModel(InviteMainModel model) {
        return model;
    }
}