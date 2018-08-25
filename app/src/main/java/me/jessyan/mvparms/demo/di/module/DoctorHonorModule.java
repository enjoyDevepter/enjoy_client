package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.DoctorHonorContract;
import me.jessyan.mvparms.demo.mvp.model.DoctorHonorModel;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorHonorBean;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DoctotHonorAdapter;


@Module
public class DoctorHonorModule {
    private DoctorHonorContract.View view;

    /**
     * 构建DoctorHonorModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public DoctorHonorModule(DoctorHonorContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    DoctorHonorContract.View provideDoctorHonorView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    DoctorHonorContract.Model provideDoctorHonorModel(DoctorHonorModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    List<DoctorHonorBean> provideCategories() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    RecyclerView.Adapter provideFilterSecondAdapter(List<DoctorHonorBean> categories) {
        return new DoctotHonorAdapter(categories);
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }
}