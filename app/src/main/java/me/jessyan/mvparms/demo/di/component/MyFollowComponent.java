package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.MyFollowModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.MyFollowActivity;

@ActivityScope
@Component(modules = MyFollowModule.class, dependencies = AppComponent.class)
public interface MyFollowComponent {
    void inject(MyFollowActivity activity);
}