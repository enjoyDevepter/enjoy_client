package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.OrderDeatilsModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.OrderDeatilsActivity;

@ActivityScope
@Component(modules = OrderDeatilsModule.class, dependencies = AppComponent.class)
public interface OrderDeatilsComponent {
    void inject(OrderDeatilsActivity activity);
}