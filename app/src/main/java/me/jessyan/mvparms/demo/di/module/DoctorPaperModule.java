package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.DoctorPaperContract;
import me.jessyan.mvparms.demo.mvp.model.DoctorPaperModel;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorIdentificationBean;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DoctorPaperAdapter;


@Module
public class DoctorPaperModule {
    private DoctorPaperContract.View view;

    /**
     * 构建DoctorPaperModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public DoctorPaperModule(DoctorPaperContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    DoctorPaperContract.View provideDoctorPaperView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    DoctorPaperContract.Model provideDoctorPaperModel(DoctorPaperModel model) {
        return model;
    }


    @ActivityScope
    @Provides
    List<DoctorIdentificationBean> provideCategories() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    RecyclerView.Adapter provideFilterSecondAdapter(List<DoctorIdentificationBean> categories) {
        return new DoctorPaperAdapter(categories);
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }
}