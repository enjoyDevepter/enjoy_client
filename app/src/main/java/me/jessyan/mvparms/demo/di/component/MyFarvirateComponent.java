package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.MyFarvirateModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.MyFarvirateActivity;

@ActivityScope
@Component(modules = MyFarvirateModule.class, dependencies = AppComponent.class)
public interface MyFarvirateComponent {
    void inject(MyFarvirateActivity activity);
}