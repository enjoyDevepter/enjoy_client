package me.jessyan.mvparms.demo.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import me.jessyan.mvparms.demo.di.module.CashPasswordModule;

import com.jess.arms.di.scope.ActivityScope;

import me.jessyan.mvparms.demo.mvp.ui.activity.CashPasswordActivity;

@ActivityScope
@Component(modules = CashPasswordModule.class, dependencies = AppComponent.class)
public interface CashPasswordComponent {
    void inject(CashPasswordActivity activity);
}