package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.ForgetModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.ForgetActivity;

@ActivityScope
@Component(modules = ForgetModule.class, dependencies = AppComponent.class)
public interface ForgetComponent {
    void inject(ForgetActivity activity);
}