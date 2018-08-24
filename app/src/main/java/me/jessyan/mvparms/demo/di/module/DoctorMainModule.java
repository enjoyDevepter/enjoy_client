package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.DoctorMainContract;
import me.jessyan.mvparms.demo.mvp.model.DoctorMainModel;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorCommentBean;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DoctorCommentHolderAdapter;


@Module
public class DoctorMainModule {
    private DoctorMainContract.View view;

    /**
     * 构建DoctorMainModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public DoctorMainModule(DoctorMainContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    DoctorMainContract.View provideDoctorMainView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    DoctorMainContract.Model provideDoctorMainModel(DoctorMainModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.Adapter provideStoreAdapter(List<DoctorCommentBean> list) {
        DoctorCommentHolderAdapter doctorCommentHolderAdapter = new DoctorCommentHolderAdapter(list);
        return doctorCommentHolderAdapter;
    }


    @ActivityScope
    @Provides
    List<DoctorCommentBean> provideOrderBeanList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }
}