package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.DiaryForGoodsModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.DiaryForGoodsActivity;

@ActivityScope
@Component(modules = DiaryForGoodsModule.class, dependencies = AppComponent.class)
public interface DiaryForGoodsComponent {
    void inject(DiaryForGoodsActivity activity);
}