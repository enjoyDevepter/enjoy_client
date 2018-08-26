package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.ConsumeCoinInputContract;
import me.jessyan.mvparms.demo.mvp.model.ConsumeCoinInputModel;


@Module
public class ConsumeCoinInputModule {
    private ConsumeCoinInputContract.View view;

    /**
     * 构建ConsumeCoinInputModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ConsumeCoinInputModule(ConsumeCoinInputContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ConsumeCoinInputContract.View provideConsumeCoinInputView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ConsumeCoinInputContract.Model provideConsumeCoinInputModel(ConsumeCoinInputModel model) {
        return model;
    }
}