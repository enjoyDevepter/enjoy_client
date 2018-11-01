package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.DoctorForHospitalContract;
import me.jessyan.mvparms.demo.mvp.model.DoctorForHospitalModel;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.HospitalBean;
import me.jessyan.mvparms.demo.mvp.ui.adapter.HospitalRelatedListAdapter;


@Module
public class DoctorForHospitalModule {
    private DoctorForHospitalContract.View view;

    /**
     * 构建DoctorForHospitalModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public DoctorForHospitalModule(DoctorForHospitalContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    DoctorForHospitalContract.View provideDoctorForHospitalView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    DoctorForHospitalContract.Model provideDoctorForHospitalModel(DoctorForHospitalModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    HospitalRelatedListAdapter provideStoreAdapter(List<HospitalBean> list) {
        return new HospitalRelatedListAdapter(list);
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @ActivityScope
    @Provides
    List<HospitalBean> provideCategories() {
        return new ArrayList<>();
    }
}