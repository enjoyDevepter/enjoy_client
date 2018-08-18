package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.PayContract;
import me.jessyan.mvparms.demo.mvp.model.PayModel;


@Module
public class PayModule {
    private PayContract.View view;

    /**
     * 构建PayModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public PayModule(PayContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    PayContract.View providePayView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    PayContract.Model providePayModel(PayModel model) {
        return model;
    }
}