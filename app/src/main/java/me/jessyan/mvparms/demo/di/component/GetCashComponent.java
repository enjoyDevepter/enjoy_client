package me.jessyan.mvparms.demo.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import me.jessyan.mvparms.demo.di.module.GetCashModule;

import com.jess.arms.di.scope.ActivityScope;

import me.jessyan.mvparms.demo.mvp.ui.activity.GetCashActivity;

@ActivityScope
@Component(modules = GetCashModule.class, dependencies = AppComponent.class)
public interface GetCashComponent {
    void inject(GetCashActivity activity);
}