package me.jessyan.mvparms.demo.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import me.jessyan.mvparms.demo.di.module.ConsumeCoinInputModule;

import com.jess.arms.di.scope.ActivityScope;

import me.jessyan.mvparms.demo.mvp.ui.activity.ConsumeCoinInputActivity;

@ActivityScope
@Component(modules = ConsumeCoinInputModule.class, dependencies = AppComponent.class)
public interface ConsumeCoinInputComponent {
    void inject(ConsumeCoinInputActivity activity);
}