package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.ChooseBankContract;
import me.jessyan.mvparms.demo.mvp.model.ChooseBankModel;


@Module
public class ChooseBankModule {
    private ChooseBankContract.View view;

    /**
     * 构建ChooseBankModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ChooseBankModule(ChooseBankContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ChooseBankContract.View provideChooseBankView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ChooseBankContract.Model provideChooseBankModel(ChooseBankModel model) {
        return model;
    }
}