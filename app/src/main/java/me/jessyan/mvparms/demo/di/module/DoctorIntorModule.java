package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.DoctorIntorContract;
import me.jessyan.mvparms.demo.mvp.model.DoctorIntorModel;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorIdentificationBean;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DoctorPaperAdapter;


@Module
public class DoctorIntorModule {
    private DoctorIntorContract.View view;

    /**
     * 构建DoctorIntorModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public DoctorIntorModule(DoctorIntorContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    DoctorIntorContract.View provideDoctorIntorView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    DoctorIntorContract.Model provideDoctorIntorModel(DoctorIntorModel model) {
        return model;
    }

}