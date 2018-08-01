package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.DiscoverModule;
import me.jessyan.mvparms.demo.mvp.ui.fragment.DiscoverFragment;

@ActivityScope
@Component(modules = DiscoverModule.class, dependencies = AppComponent.class)
public interface DiscoverComponent {
    void inject(DiscoverFragment fragment);
}