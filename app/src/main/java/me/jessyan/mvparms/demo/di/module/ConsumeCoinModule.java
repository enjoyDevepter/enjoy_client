package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.ConsumeCoinContract;
import me.jessyan.mvparms.demo.mvp.model.ConsumeCoinModel;


@Module
public class ConsumeCoinModule {
    private ConsumeCoinContract.View view;

    /**
     * 构建ConsumeCoinModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ConsumeCoinModule(ConsumeCoinContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ConsumeCoinContract.View provideConsumeCoinView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ConsumeCoinContract.Model provideConsumeCoinModel(ConsumeCoinModel model) {
        return model;
    }
}