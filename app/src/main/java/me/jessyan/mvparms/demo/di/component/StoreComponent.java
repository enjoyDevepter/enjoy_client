package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.StoreModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.StoreActivity;

@ActivityScope
@Component(modules = StoreModule.class, dependencies = AppComponent.class)
public interface StoreComponent {
    void inject(StoreActivity activity);
}