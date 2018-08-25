package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.MyDiaryModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.MyDiaryActivity;

@ActivityScope
@Component(modules = MyDiaryModule.class, dependencies = AppComponent.class)
public interface MyDiaryComponent {
    void inject(MyDiaryActivity activity);
}