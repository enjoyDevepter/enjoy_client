package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.ModifyUserInfoModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.ModifyUserInfoActivity;

@ActivityScope
@Component(modules = ModifyUserInfoModule.class, dependencies = AppComponent.class)
public interface ModifyUserInfoComponent {
    void inject(ModifyUserInfoActivity activity);
}