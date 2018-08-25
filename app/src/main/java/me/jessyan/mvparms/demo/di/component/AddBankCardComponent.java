package me.jessyan.mvparms.demo.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import me.jessyan.mvparms.demo.di.module.AddBankCardModule;

import com.jess.arms.di.scope.ActivityScope;

import me.jessyan.mvparms.demo.mvp.ui.activity.AddBankCardActivity;

@ActivityScope
@Component(modules = AddBankCardModule.class, dependencies = AppComponent.class)
public interface AddBankCardComponent {
    void inject(AddBankCardActivity activity);
}