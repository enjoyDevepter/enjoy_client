package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.ModifyUserInfoContract;
import me.jessyan.mvparms.demo.mvp.model.ModifyUserInfoModel;


@Module
public class ModifyUserInfoModule {
    private ModifyUserInfoContract.View view;

    /**
     * 构建ModifyUserInfoModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ModifyUserInfoModule(ModifyUserInfoContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ModifyUserInfoContract.View provideModifyUserInfoView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ModifyUserInfoContract.Model provideModifyUserInfoModel(ModifyUserInfoModel model) {
        return model;
    }
}