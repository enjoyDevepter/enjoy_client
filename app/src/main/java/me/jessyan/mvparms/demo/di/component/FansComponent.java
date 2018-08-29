package me.jessyan.mvparms.demo.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import me.jessyan.mvparms.demo.di.module.FansModule;

import com.jess.arms.di.scope.ActivityScope;

import me.jessyan.mvparms.demo.mvp.ui.activity.FansActivity;

@ActivityScope
@Component(modules = FansModule.class, dependencies = AppComponent.class)
public interface FansComponent {
    void inject(FansActivity activity);
}