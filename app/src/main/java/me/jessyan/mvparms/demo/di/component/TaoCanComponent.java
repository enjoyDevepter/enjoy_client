package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.TaoCanModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.TaoCanActivity;

@ActivityScope
@Component(modules = TaoCanModule.class, dependencies = AppComponent.class)
public interface TaoCanComponent {
    void inject(TaoCanActivity activity);
}