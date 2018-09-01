package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.MyStoreModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.MyStoreActivity;

@ActivityScope
@Component(modules = MyStoreModule.class, dependencies = AppComponent.class)
public interface MyStoreComponent {
    void inject(MyStoreActivity activity);
}