package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.ModifyModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.ModifyActivity;

@ActivityScope
@Component(modules = ModifyModule.class, dependencies = AppComponent.class)
public interface ModifyComponent {
    void inject(ModifyActivity activity);
}