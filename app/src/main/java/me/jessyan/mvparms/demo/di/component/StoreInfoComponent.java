package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.StoreInfoModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.StoreInfoActivity;

@ActivityScope
@Component(modules = StoreInfoModule.class, dependencies = AppComponent.class)
public interface StoreInfoComponent {
    void inject(StoreInfoActivity activity);
}