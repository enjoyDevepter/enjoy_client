package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.ReleaseDiaryModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.ReleaseDiaryActivity;

@ActivityScope
@Component(modules = ReleaseDiaryModule.class, dependencies = AppComponent.class)
public interface ReleaseDiaryComponent {
    void inject(ReleaseDiaryActivity activity);
}