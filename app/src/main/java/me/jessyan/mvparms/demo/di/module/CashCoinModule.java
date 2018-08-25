package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.CashCoinContract;
import me.jessyan.mvparms.demo.mvp.model.CashCoinModel;


@Module
public class CashCoinModule {
    private CashCoinContract.View view;

    /**
     * 构建CashCoinModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public CashCoinModule(CashCoinContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    CashCoinContract.View provideCashCoinView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    CashCoinContract.Model provideCashCoinModel(CashCoinModel model) {
        return model;
    }
}