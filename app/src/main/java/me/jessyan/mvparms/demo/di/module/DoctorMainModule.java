package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.DoctorMainContract;
import me.jessyan.mvparms.demo.mvp.model.DoctorMainModel;


@Module
public class DoctorMainModule {
    private DoctorMainContract.View view;

    /**
     * 构建DoctorMainModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public DoctorMainModule(DoctorMainContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    DoctorMainContract.View provideDoctorMainView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    DoctorMainContract.Model provideDoctorMainModel(DoctorMainModel model) {
        return model;
    }
}