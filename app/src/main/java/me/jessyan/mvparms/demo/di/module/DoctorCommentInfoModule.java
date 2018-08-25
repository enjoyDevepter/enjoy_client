package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.DoctorCommentInfoContract;
import me.jessyan.mvparms.demo.mvp.model.DoctorCommentInfoModel;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorCommentReplyBean;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DoctorCommentReplyAdapter;


@Module
public class DoctorCommentInfoModule {
    private DoctorCommentInfoContract.View view;

    /**
     * 构建DoctorCommentInfoModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public DoctorCommentInfoModule(DoctorCommentInfoContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    DoctorCommentInfoContract.View provideDoctorCommentInfoView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    DoctorCommentInfoContract.Model provideDoctorCommentInfoModel(DoctorCommentInfoModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.Adapter provideStoreAdapter(List<DoctorCommentReplyBean> list) {
        return new DoctorCommentReplyAdapter(list);
    }


    @ActivityScope
    @Provides
    List<DoctorCommentReplyBean> provideOrderBeanList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

}