package me.jessyan.mvparms.demo.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import me.jessyan.mvparms.demo.di.module.InviteMainModule;

import com.jess.arms.di.scope.ActivityScope;

import me.jessyan.mvparms.demo.mvp.ui.activity.InviteMainActivity;

@ActivityScope
@Component(modules = InviteMainModule.class, dependencies = AppComponent.class)
public interface InviteMainComponent {
    void inject(InviteMainActivity activity);
}