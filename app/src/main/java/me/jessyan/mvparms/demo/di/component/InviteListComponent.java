package me.jessyan.mvparms.demo.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import me.jessyan.mvparms.demo.di.module.InviteListModule;

import com.jess.arms.di.scope.ActivityScope;

import me.jessyan.mvparms.demo.mvp.ui.activity.InviteListActivity;

@ActivityScope
@Component(modules = InviteListModule.class, dependencies = AppComponent.class)
public interface InviteListComponent {
    void inject(InviteListActivity activity);
}