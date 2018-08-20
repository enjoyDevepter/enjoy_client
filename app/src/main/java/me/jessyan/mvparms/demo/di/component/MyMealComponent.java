package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.MyMealModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.MyMealActivity;

@ActivityScope
@Component(modules = MyMealModule.class, dependencies = AppComponent.class)
public interface MyMealComponent {
    void inject(MyMealActivity activity);
}