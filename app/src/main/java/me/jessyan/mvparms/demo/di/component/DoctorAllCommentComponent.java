package me.jessyan.mvparms.demo.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import me.jessyan.mvparms.demo.di.module.DoctorAllCommentModule;

import com.jess.arms.di.scope.ActivityScope;

import me.jessyan.mvparms.demo.mvp.ui.activity.DoctorAllCommentActivity;

@ActivityScope
@Component(modules = DoctorAllCommentModule.class, dependencies = AppComponent.class)
public interface DoctorAllCommentComponent {
    void inject(DoctorAllCommentActivity activity);
}