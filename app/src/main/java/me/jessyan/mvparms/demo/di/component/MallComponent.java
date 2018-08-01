package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.MallModule;
import me.jessyan.mvparms.demo.mvp.ui.fragment.MallFragment;

@ActivityScope
@Component(modules = MallModule.class, dependencies = AppComponent.class)
public interface MallComponent {
    void inject(MallFragment fragment);
}