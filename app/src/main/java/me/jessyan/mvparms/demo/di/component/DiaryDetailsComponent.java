package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.DiaryDetailsModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.DiaryDetailsActivity;

@ActivityScope
@Component(modules = DiaryDetailsModule.class, dependencies = AppComponent.class)
public interface DiaryDetailsComponent {
    void inject(DiaryDetailsActivity activity);
}