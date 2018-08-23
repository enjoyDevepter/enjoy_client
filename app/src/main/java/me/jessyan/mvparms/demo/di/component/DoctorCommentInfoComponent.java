package me.jessyan.mvparms.demo.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import me.jessyan.mvparms.demo.di.module.DoctorCommentInfoModule;

import com.jess.arms.di.scope.ActivityScope;

import me.jessyan.mvparms.demo.mvp.ui.activity.DoctorCommentInfoActivity;

@ActivityScope
@Component(modules = DoctorCommentInfoModule.class, dependencies = AppComponent.class)
public interface DoctorCommentInfoComponent {
    void inject(DoctorCommentInfoActivity activity);
}