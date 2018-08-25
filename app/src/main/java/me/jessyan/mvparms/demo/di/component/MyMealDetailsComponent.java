package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.MyMealDetailsModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.MyMealDetailsActivity;

@ActivityScope
@Component(modules = MyMealDetailsModule.class, dependencies = AppComponent.class)
public interface MyMealDetailsComponent {
    void inject(MyMealDetailsActivity activity);
}