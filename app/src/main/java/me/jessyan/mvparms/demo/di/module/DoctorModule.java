package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.DoctorContract;
import me.jessyan.mvparms.demo.mvp.model.DoctorModel;
import me.jessyan.mvparms.demo.mvp.model.entity.Category;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorBean;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DoctorListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsFilterSecondAdapter;


@Module
public class DoctorModule {
    private DoctorContract.View view;

    /**
     * 构建DoctorModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public DoctorModule(DoctorContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    DoctorContract.View provideDoctorView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    DoctorContract.Model provideDoctorModel(DoctorModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @ActivityScope
    @Provides
    List<Category> provideCategories() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    GoodsFilterSecondAdapter provideFilterSecondAdapter(List<Category> categories) {
        return new GoodsFilterSecondAdapter(categories);
    }

    @ActivityScope
    @Provides
    List<DoctorBean> provideDoctorList() {
        ArrayList<DoctorBean> orderBeans = new ArrayList<>();
        return orderBeans;
    }

    @ActivityScope
    @Provides
    DoctorListAdapter provideDoctorAdapter(List<DoctorBean> list) {
        return new DoctorListAdapter(list);
    }

}