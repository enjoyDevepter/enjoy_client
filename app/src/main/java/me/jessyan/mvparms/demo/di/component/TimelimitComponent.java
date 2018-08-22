package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.TimelimitModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.TimelimitActivity;

@ActivityScope
@Component(modules = TimelimitModule.class, dependencies = AppComponent.class)
public interface TimelimitComponent {
    void inject(TimelimitActivity activity);
}