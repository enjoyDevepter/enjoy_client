package me.jessyan.mvparms.demo.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import me.jessyan.mvparms.demo.di.module.ChooseBankModule;

import com.jess.arms.di.scope.ActivityScope;

import me.jessyan.mvparms.demo.mvp.ui.activity.ChooseBankActivity;

@ActivityScope
@Component(modules = ChooseBankModule.class, dependencies = AppComponent.class)
public interface ChooseBankComponent {
    void inject(ChooseBankActivity activity);
}