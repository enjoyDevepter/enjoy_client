package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.MyOrderModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.MyOrderActivity;

@ActivityScope
@Component(modules = MyOrderModule.class, dependencies = AppComponent.class)
public interface MyOrderComponent {
    void inject(MyOrderActivity activity);
}