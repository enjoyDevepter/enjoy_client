package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.MealOrderConfirmModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.MealOrderConfirmActivity;

@ActivityScope
@Component(modules = MealOrderConfirmModule.class, dependencies = AppComponent.class)
public interface MealOrderConfirmComponent {
    void inject(MealOrderConfirmActivity activity);
}