package me.jessyan.mvparms.demo.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import me.jessyan.mvparms.demo.di.module.HospitalChooseModule;

import com.jess.arms.di.scope.ActivityScope;

import me.jessyan.mvparms.demo.mvp.ui.activity.HospitalChooseActivity;

@ActivityScope
@Component(modules = HospitalChooseModule.class, dependencies = AppComponent.class)
public interface HospitalChooseComponent {
    void inject(HospitalChooseActivity activity);
}