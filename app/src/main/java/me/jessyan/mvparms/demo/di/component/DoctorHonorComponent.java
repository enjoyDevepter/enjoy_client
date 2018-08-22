package me.jessyan.mvparms.demo.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import me.jessyan.mvparms.demo.di.module.DoctorHonorModule;

import com.jess.arms.di.scope.ActivityScope;

import me.jessyan.mvparms.demo.mvp.ui.activity.DoctorHonorActivity;

@ActivityScope
@Component(modules = DoctorHonorModule.class, dependencies = AppComponent.class)
public interface DoctorHonorComponent {
    void inject(DoctorHonorActivity activity);
}