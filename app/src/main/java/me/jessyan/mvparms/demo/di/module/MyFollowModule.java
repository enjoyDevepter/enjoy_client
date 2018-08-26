package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.MyFollowContract;
import me.jessyan.mvparms.demo.mvp.model.MyFollowModel;
import me.jessyan.mvparms.demo.mvp.model.entity.Hospital;
import me.jessyan.mvparms.demo.mvp.model.entity.Member;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorBean;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyFollowDoctorAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyFollowHospitalAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyFollowMemberAdapter;


@Module
public class MyFollowModule {
    private MyFollowContract.View view;

    /**
     * 构建MyFollowModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MyFollowModule(MyFollowContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MyFollowContract.View provideMyFollowView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MyFollowContract.Model provideMyFollowModel(MyFollowModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @ActivityScope
    @Provides
    List<Member> provideMemberList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    List<DoctorBean> provideDoctorList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    List<Hospital> provideHospitalList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    MyFollowMemberAdapter provideMemberListAdapter(List<Member> members) {
        return new MyFollowMemberAdapter(members);
    }

    @ActivityScope
    @Provides
    MyFollowDoctorAdapter provideDoctorListAdapter(List<DoctorBean> members) {
        return new MyFollowDoctorAdapter(members);
    }

    @ActivityScope
    @Provides
    MyFollowHospitalAdapter provideHospitalListAdapter(List<Hospital> members) {
        return new MyFollowHospitalAdapter(members);
    }

}