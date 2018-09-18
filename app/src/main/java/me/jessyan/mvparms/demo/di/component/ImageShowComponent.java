package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.ImageShowModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.ImageShowActivity;

@ActivityScope
@Component(modules = ImageShowModule.class, dependencies = AppComponent.class)
public interface ImageShowComponent {
    void inject(ImageShowActivity activity);
}