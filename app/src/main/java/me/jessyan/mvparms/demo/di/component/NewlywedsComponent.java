package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.NewlywedsModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.NewlywedsActivity;

@ActivityScope
@Component(modules = NewlywedsModule.class, dependencies = AppComponent.class)
public interface NewlywedsComponent {
    void inject(NewlywedsActivity activity);
}