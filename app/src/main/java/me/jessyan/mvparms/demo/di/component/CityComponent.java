package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.CityModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.CityActivity;

@ActivityScope
@Component(modules = CityModule.class, dependencies = AppComponent.class)
public interface CityComponent {
    void inject(CityActivity activity);
}