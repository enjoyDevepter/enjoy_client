package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.CashPasswordContract;
import me.jessyan.mvparms.demo.mvp.model.CashPasswordModel;


@Module
public class CashPasswordModule {
    private CashPasswordContract.View view;

    /**
     * 构建CashPasswordModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public CashPasswordModule(CashPasswordContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    CashPasswordContract.View provideCashPasswordView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    CashPasswordContract.Model provideCashPasswordModel(CashPasswordModel model) {
        return model;
    }
}