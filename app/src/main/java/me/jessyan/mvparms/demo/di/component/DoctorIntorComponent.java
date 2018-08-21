package me.jessyan.mvparms.demo.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import me.jessyan.mvparms.demo.di.module.DoctorIntorModule;

import com.jess.arms.di.scope.ActivityScope;

import me.jessyan.mvparms.demo.mvp.ui.activity.DoctorIntorActivity;

@ActivityScope
@Component(modules = DoctorIntorModule.class, dependencies = AppComponent.class)
public interface DoctorIntorComponent {
    void inject(DoctorIntorActivity activity);
}