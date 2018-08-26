package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.ModifyPasswordContract;
import me.jessyan.mvparms.demo.mvp.model.ModifyPasswordModel;


@Module
public class ModifyPasswordModule {
    private ModifyPasswordContract.View view;

    /**
     * 构建ModifyPasswordModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ModifyPasswordModule(ModifyPasswordContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ModifyPasswordContract.View provideModifyPasswordView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ModifyPasswordContract.Model provideModifyPasswordModel(ModifyPasswordModel model) {
        return model;
    }
}