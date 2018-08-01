package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.SearchResultModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.SearchResultActivity;

@ActivityScope
@Component(modules = SearchResultModule.class, dependencies = AppComponent.class)
public interface SerachResultComponent {
    void inject(SearchResultActivity activity);
}