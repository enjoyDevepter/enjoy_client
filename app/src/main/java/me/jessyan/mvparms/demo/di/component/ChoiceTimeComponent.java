package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.ChoiceTimeModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.ChoiceTimeActivity;

@ActivityScope
@Component(modules = ChoiceTimeModule.class, dependencies = AppComponent.class)
public interface ChoiceTimeComponent {
    void inject(ChoiceTimeActivity activity);
}