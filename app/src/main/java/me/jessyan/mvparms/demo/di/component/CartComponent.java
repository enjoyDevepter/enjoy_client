package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.CartModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.CartActivity;

@ActivityScope
@Component(modules = CartModule.class, dependencies = AppComponent.class)
public interface CartComponent {
    void inject(CartActivity activity);
}