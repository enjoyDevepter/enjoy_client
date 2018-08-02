package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.AddressListModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.AddressListActivity;

@ActivityScope
@Component(modules = AddressListModule.class, dependencies = AppComponent.class)
public interface AddressListComponent {
    void inject(AddressListActivity activity);
}