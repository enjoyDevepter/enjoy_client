package me.jessyan.mvparms.demo.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import me.jessyan.mvparms.demo.di.module.HospitalInfoModule;

import com.jess.arms.di.scope.ActivityScope;

import me.jessyan.mvparms.demo.mvp.ui.activity.HospitalInfoActivity;

@ActivityScope
@Component(modules = HospitalInfoModule.class, dependencies = AppComponent.class)
public interface HospitalInfoComponent {
    void inject(HospitalInfoActivity activity);
}