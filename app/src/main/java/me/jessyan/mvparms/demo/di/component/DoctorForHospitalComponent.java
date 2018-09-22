package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.DoctorForHospitalModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.DoctorForHospitalActivity;

@ActivityScope
@Component(modules = DoctorForHospitalModule.class, dependencies = AppComponent.class)
public interface DoctorForHospitalComponent {
    void inject(DoctorForHospitalActivity activity);
}