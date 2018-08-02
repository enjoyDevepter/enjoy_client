package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.PayResultContract;
import me.jessyan.mvparms.demo.mvp.model.PayResultModel;


@Module
public class PayResultModule {
    private PayResultContract.View view;

    /**
     * 构建PayResultModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public PayResultModule(PayResultContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    PayResultContract.View providePayResultView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    PayResultContract.Model providePayResultModel(PayResultModel model) {
        return model;
    }
}