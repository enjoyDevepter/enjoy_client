package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.ActivityInfoModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.ActivityInfoActivity;

@ActivityScope
@Component(modules = ActivityInfoModule.class, dependencies = AppComponent.class)
public interface ActivityInfoComponent {
    void inject(ActivityInfoActivity activity);
}