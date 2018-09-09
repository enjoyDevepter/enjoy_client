package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.RechargeFinishedModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.RechargeFinishedActivity;

@ActivityScope
@Component(modules = RechargeFinishedModule.class, dependencies = AppComponent.class)
public interface RechargeFinishedComponent {
    void inject(RechargeFinishedActivity activity);
}