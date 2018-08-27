package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;
import com.tbruyelle.rxpermissions2.RxPermissions;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.AuthenticationContract;
import me.jessyan.mvparms.demo.mvp.model.AuthenticationModel;


@Module
public class AuthenticationModule {
    private AuthenticationContract.View view;

    /**
     * 构建AuthenticationModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public AuthenticationModule(AuthenticationContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    AuthenticationContract.View provideAuthenticationView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    AuthenticationContract.Model provideAuthenticationModel(AuthenticationModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RxPermissions provideRxPermissions() {
        return new RxPermissions(view.getActivity());
    }
}