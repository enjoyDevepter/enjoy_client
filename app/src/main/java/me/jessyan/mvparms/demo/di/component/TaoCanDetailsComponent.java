package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.TaoCanDetailsModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.TaoCanDetailsActivity;

@ActivityScope
@Component(modules = TaoCanDetailsModule.class, dependencies = AppComponent.class)
public interface TaoCanDetailsComponent {
    void inject(TaoCanDetailsActivity activity);
}