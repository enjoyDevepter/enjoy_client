package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.SelfPickupAddrListModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.SelfPickupAddrListActivity;

@ActivityScope
@Component(modules = SelfPickupAddrListModule.class, dependencies = AppComponent.class)
public interface SelfPickupAddrListComponent {
    void inject(SelfPickupAddrListActivity activity);
}