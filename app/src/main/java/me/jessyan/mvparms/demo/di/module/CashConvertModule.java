package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.CashConvertContract;
import me.jessyan.mvparms.demo.mvp.model.CashConvertModel;


@Module
public class CashConvertModule {
    private CashConvertContract.View view;

    /**
     * 构建CashConvertModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public CashConvertModule(CashConvertContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    CashConvertContract.View provideCashConvertView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    CashConvertContract.Model provideCashConvertModel(CashConvertModel model) {
        return model;
    }
}