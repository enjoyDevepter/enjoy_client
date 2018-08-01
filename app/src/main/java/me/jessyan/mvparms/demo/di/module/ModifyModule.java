package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.ModifyContract;
import me.jessyan.mvparms.demo.mvp.model.ModifyModel;


@Module
public class ModifyModule {
    private ModifyContract.View view;

    /**
     * 构建ModifyModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ModifyModule(ModifyContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ModifyContract.View provideModifyView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ModifyContract.Model provideModifyModel(ModifyModel model) {
        return model;
    }
}