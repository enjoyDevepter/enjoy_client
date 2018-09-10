package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.PickCouponModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.PickCouponActivity;

@ActivityScope
@Component(modules = PickCouponModule.class, dependencies = AppComponent.class)
public interface PickCouponComponent {
    void inject(PickCouponActivity activity);
}