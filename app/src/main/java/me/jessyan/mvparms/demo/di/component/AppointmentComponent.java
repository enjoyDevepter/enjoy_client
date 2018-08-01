package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.AppointmentModule;
import me.jessyan.mvparms.demo.mvp.ui.fragment.AppointmentFragment;

@ActivityScope
@Component(modules = AppointmentModule.class, dependencies = AppComponent.class)
public interface AppointmentComponent {
    void inject(AppointmentFragment fragment);
}