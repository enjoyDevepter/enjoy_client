package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.KGoodsOrderConfirmModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.KGoodsOrderConfirmActivity;

@ActivityScope
@Component(modules = KGoodsOrderConfirmModule.class, dependencies = AppComponent.class)
public interface KGoodsOrderConfirmComponent {
    void inject(KGoodsOrderConfirmActivity activity);
}