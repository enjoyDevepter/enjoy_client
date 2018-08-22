package me.jessyan.mvparms.demo.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import me.jessyan.mvparms.demo.di.module.DoctorPaperModule;

import com.jess.arms.di.scope.ActivityScope;

import me.jessyan.mvparms.demo.mvp.ui.activity.DoctorPaperActivity;

@ActivityScope
@Component(modules = DoctorPaperModule.class, dependencies = AppComponent.class)
public interface DoctorPaperComponent {
    void inject(DoctorPaperActivity activity);
}