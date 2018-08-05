package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.ConfirmOrderModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.ConfirmOrderActivity;

@ActivityScope
@Component(modules = ConfirmOrderModule.class, dependencies = AppComponent.class)
public interface ConfirmOrderComponent {
    void inject(ConfirmOrderActivity activity);
}