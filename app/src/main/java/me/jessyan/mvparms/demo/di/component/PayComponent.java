package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.PayModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.PayActivity;

@ActivityScope
@Component(modules = PayModule.class, dependencies = AppComponent.class)
public interface PayComponent {
    void inject(PayActivity activity);
}