package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.ChoiceStoreModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.ChoiceStoreActivity;

@ActivityScope
@Component(modules = ChoiceStoreModule.class, dependencies = AppComponent.class)
public interface ChoiceStoreComponent {
    void inject(ChoiceStoreActivity activity);
}