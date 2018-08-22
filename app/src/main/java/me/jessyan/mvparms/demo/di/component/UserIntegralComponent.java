package me.jessyan.mvparms.demo.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import me.jessyan.mvparms.demo.di.module.UserIntegralModule;

import com.jess.arms.di.scope.ActivityScope;

import me.jessyan.mvparms.demo.mvp.ui.activity.UserIntegralActivity;

@ActivityScope
@Component(modules = UserIntegralModule.class, dependencies = AppComponent.class)
public interface UserIntegralComponent {
    void inject(UserIntegralActivity activity);
}