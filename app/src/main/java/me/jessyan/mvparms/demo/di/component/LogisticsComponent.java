package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.LogisticsModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.LogisticsActivity;

@ActivityScope
@Component(modules = LogisticsModule.class, dependencies = AppComponent.class)
public interface LogisticsComponent {
    void inject(LogisticsActivity activity);
}