package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.DoctorAllCommentContract;
import me.jessyan.mvparms.demo.mvp.model.DoctorAllCommentModel;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorCommentBean;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DoctorCommentHolderAdapter;


@Module
public class DoctorAllCommentModule {
    private DoctorAllCommentContract.View view;

    /**
     * 构建DoctorAllCommentModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public DoctorAllCommentModule(DoctorAllCommentContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    DoctorAllCommentContract.View provideDoctorAllCommentView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    DoctorAllCommentContract.Model provideDoctorAllCommentModel(DoctorAllCommentModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.Adapter provideStoreAdapter(List<DoctorCommentBean> list) {
        return new DoctorCommentHolderAdapter(list);
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