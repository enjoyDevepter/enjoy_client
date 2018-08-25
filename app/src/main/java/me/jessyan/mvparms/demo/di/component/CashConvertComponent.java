package me.jessyan.mvparms.demo.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import me.jessyan.mvparms.demo.di.module.CashConvertModule;

import com.jess.arms.di.scope.ActivityScope;

import me.jessyan.mvparms.demo.mvp.ui.activity.CashConvertActivity;

@ActivityScope
@Component(modules = CashConvertModule.class, dependencies = AppComponent.class)
public interface CashConvertComponent {
    void inject(CashConvertActivity activity);
}