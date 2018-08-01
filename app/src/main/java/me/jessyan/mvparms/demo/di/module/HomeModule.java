package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;
import com.tbruyelle.rxpermissions2.RxPermissions;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.HomeContract;
import me.jessyan.mvparms.demo.mvp.model.HomeModel;


@Module
public class HomeModule {
    private HomeContract.View view;

    /**
     * 构建HomeModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public HomeModule(HomeContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    HomeContract.View provideHomeView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    HomeContract.Model provideHomeModel(HomeModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RxPermissions provideRxPermissions() {
        return new RxPermissions(view.getActivity());
    }

}