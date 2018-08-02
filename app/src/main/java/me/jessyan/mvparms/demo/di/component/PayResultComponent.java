package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.PayResultModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.PayResultActivity;

@ActivityScope
@Component(modules = PayResultModule.class, dependencies = AppComponent.class)
public interface PayResultComponent {
    void inject(PayResultActivity activity);
}