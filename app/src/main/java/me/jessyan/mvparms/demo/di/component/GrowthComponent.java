package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.GrowthModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.GrowthActivity;

@ActivityScope
@Component(modules = GrowthModule.class, dependencies = AppComponent.class)
public interface GrowthComponent {
    void inject(GrowthActivity activity);
}