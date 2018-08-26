package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.AuthenticationModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.AuthenticationActivity;

@ActivityScope
@Component(modules = AuthenticationModule.class, dependencies = AppComponent.class)
public interface AuthenticationComponent {
    void inject(AuthenticationActivity activity);
}