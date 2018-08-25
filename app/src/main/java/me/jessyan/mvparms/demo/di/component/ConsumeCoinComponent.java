package me.jessyan.mvparms.demo.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import me.jessyan.mvparms.demo.di.module.ConsumeCoinModule;

import com.jess.arms.di.scope.ActivityScope;

import me.jessyan.mvparms.demo.mvp.ui.activity.ConsumeCoinActivity;

@ActivityScope
@Component(modules = ConsumeCoinModule.class, dependencies = AppComponent.class)
public interface ConsumeCoinComponent {
    void inject(ConsumeCoinActivity activity);
}