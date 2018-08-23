package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.DoctorCommentInfoContract;
import me.jessyan.mvparms.demo.mvp.model.DoctorCommentInfoModel;


@Module
public class DoctorCommentInfoModule {
    private DoctorCommentInfoContract.View view;

    /**
     * 构建DoctorCommentInfoModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public DoctorCommentInfoModule(DoctorCommentInfoContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    DoctorCommentInfoContract.View provideDoctorCommentInfoView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    DoctorCommentInfoContract.Model provideDoctorCommentInfoModel(DoctorCommentInfoModel model) {
        return model;
    }
}