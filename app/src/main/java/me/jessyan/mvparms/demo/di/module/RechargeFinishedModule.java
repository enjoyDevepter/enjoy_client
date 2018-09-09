package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.RechargeFinishedContract;
import me.jessyan.mvparms.demo.mvp.model.RechargeFinishedModel;


@Module
public class RechargeFinishedModule {
    private RechargeFinishedContract.View view;

    /**
     * 构建RechargeFinishedModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public RechargeFinishedModule(RechargeFinishedContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    RechargeFinishedContract.View provideRechargeFinishedView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    RechargeFinishedContract.Model provideRechargeFinishedModel(RechargeFinishedModel model) {
        return model;
    }
}