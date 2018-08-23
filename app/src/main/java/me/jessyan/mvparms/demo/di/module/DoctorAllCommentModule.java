package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.DoctorAllCommentContract;
import me.jessyan.mvparms.demo.mvp.model.DoctorAllCommentModel;


@Module
public class DoctorAllCommentModule {
    private DoctorAllCommentContract.View view;

    /**
     * 构建DoctorAllCommentModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public DoctorAllCommentModule(DoctorAllCommentContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    DoctorAllCommentContract.View provideDoctorAllCommentView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    DoctorAllCommentContract.Model provideDoctorAllCommentModel(DoctorAllCommentModel model) {
        return model;
    }
}