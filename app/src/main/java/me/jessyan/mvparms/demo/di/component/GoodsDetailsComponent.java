package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.GoodsDetailsModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.GoodsDetailsActivity;

@ActivityScope
@Component(modules = GoodsDetailsModule.class, dependencies = AppComponent.class)
public interface GoodsDetailsComponent {
    void inject(GoodsDetailsActivity activity);
}