package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.KGoodsDetailsActivityModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.KGoodsDetailsActivityActivity;

@ActivityScope
@Component(modules = KGoodsDetailsActivityModule.class, dependencies = AppComponent.class)
public interface KGoodsDetailsActivityComponent {
    void inject(KGoodsDetailsActivityActivity activity);
}