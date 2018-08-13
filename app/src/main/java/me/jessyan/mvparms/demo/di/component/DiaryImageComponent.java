package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.DiaryImageModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.DiaryImageActivity;

@ActivityScope
@Component(modules = DiaryImageModule.class, dependencies = AppComponent.class)
public interface DiaryImageComponent {
    void inject(DiaryImageActivity activity);
}