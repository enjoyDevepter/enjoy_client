package me.jessyan.mvparms.demo.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import me.jessyan.mvparms.demo.di.module.FeedBackModule;

import com.jess.arms.di.scope.ActivityScope;

import me.jessyan.mvparms.demo.mvp.ui.activity.FeedBackActivity;

@ActivityScope
@Component(modules = FeedBackModule.class, dependencies = AppComponent.class)
public interface FeedBackComponent {
    void inject(FeedBackActivity activity);
}