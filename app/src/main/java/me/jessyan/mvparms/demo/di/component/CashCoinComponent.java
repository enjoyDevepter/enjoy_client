package me.jessyan.mvparms.demo.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import me.jessyan.mvparms.demo.di.module.CashCoinModule;

import com.jess.arms.di.scope.ActivityScope;

import me.jessyan.mvparms.demo.mvp.ui.activity.CashCoinActivity;

@ActivityScope
@Component(modules = CashCoinModule.class, dependencies = AppComponent.class)
public interface CashCoinComponent {
    void inject(CashCoinActivity activity);
}