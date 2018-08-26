package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.PlatformModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.PlatformActivity;

@ActivityScope
@Component(modules = PlatformModule.class, dependencies = AppComponent.class)
public interface PlatformComponent {
    void inject(PlatformActivity activity);
}