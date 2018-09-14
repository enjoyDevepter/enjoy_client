package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.DoctorModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.DoctorActivity;

@ActivityScope
@Component(modules = DoctorModule.class, dependencies = AppComponent.class)
public interface DoctorComponent {
    void inject(DoctorActivity activity);
}