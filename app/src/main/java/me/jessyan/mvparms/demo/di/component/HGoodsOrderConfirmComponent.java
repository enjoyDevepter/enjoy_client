package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.HGoodsOrderConfirmModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.HGoodsOrderConfirmActivity;

@ActivityScope
@Component(modules = HGoodsOrderConfirmModule.class, dependencies = AppComponent.class)
public interface HGoodsOrderConfirmComponent {
    void inject(HGoodsOrderConfirmActivity activity);
}