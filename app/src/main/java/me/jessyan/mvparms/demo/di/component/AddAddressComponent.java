package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.AddAddressModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.AddAddressActivity;

@ActivityScope
@Component(modules = AddAddressModule.class, dependencies = AppComponent.class)
public interface AddAddressComponent {
    void inject(AddAddressActivity activity);
}