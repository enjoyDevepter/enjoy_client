package me.jessyan.mvparms.demo.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import me.jessyan.mvparms.demo.di.module.DoctorMainModule;

import com.jess.arms.di.scope.ActivityScope;

import me.jessyan.mvparms.demo.mvp.ui.activity.DoctorMainActivity;

@ActivityScope
@Component(modules = DoctorMainModule.class, dependencies = AppComponent.class)
public interface DoctorMainComponent {
    void inject(DoctorMainActivity activity);
}