package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.MyModule;
import me.jessyan.mvparms.demo.mvp.ui.fragment.MyFragment;

@ActivityScope
@Component(modules = MyModule.class, dependencies = AppComponent.class)
public interface MyComponent {
    void inject(MyFragment fragment);
}