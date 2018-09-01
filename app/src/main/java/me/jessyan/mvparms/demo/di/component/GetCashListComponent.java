package me.jessyan.mvparms.demo.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import me.jessyan.mvparms.demo.di.module.GetCashListModule;

import com.jess.arms.di.scope.ActivityScope;

import me.jessyan.mvparms.demo.mvp.ui.activity.GetCashListActivity;

@ActivityScope
@Component(modules = GetCashListModule.class, dependencies = AppComponent.class)
public interface GetCashListComponent {
    void inject(GetCashListActivity activity);
}