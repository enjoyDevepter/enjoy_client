package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.HospitalModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.HospitalActivity;

@ActivityScope
@Component(modules = HospitalModule.class, dependencies = AppComponent.class)
public interface HospitalComponent {
    void inject(HospitalActivity activity);
}