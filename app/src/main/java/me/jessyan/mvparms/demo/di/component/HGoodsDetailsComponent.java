package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.HGoodsDetailsModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.HGoodsDetailsActivity;

@ActivityScope
@Component(modules = HGoodsDetailsModule.class, dependencies = AppComponent.class)
public interface HGoodsDetailsComponent {
    void inject(HGoodsDetailsActivity activity);
}