package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.WebContract;
import me.jessyan.mvparms.demo.mvp.model.WebModel;


@Module
public class WebModule {
    private WebContract.View view;

    /**
     * 构建WebModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public WebModule(WebContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    WebContract.View provideWebView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    WebContract.Model provideWebModel(WebModel model) {
        return model;
    }
}