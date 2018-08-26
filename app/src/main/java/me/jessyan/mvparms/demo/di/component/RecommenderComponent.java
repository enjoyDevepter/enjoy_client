package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.RecommenderModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.RecommenderActivity;

@ActivityScope
@Component(modules = RecommenderModule.class, dependencies = AppComponent.class)
public interface RecommenderComponent {
    void inject(RecommenderActivity activity);
}