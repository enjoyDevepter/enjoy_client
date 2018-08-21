package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.HospitalInfoContract;
import me.jessyan.mvparms.demo.mvp.model.HospitalInfoModel;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorBean;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DoctorListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.HospitalEnvImageAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.HospitalGoodsListAdapter;


@Module
public class HospitalInfoModule {
    private HospitalInfoContract.View view;

    /**
     * 构建HospitalInfoModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public HospitalInfoModule(HospitalInfoContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    HospitalInfoContract.View provideHospitalInfoView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    HospitalInfoContract.Model provideHospitalInfoModel(HospitalInfoModel model) {
        return model;
    }

    // 第二个页面
    @ActivityScope
    @Provides
    List<Goods> provideGoodsList() {
        ArrayList<Goods> orderBeans = new ArrayList<>();
        return orderBeans;
    }
    @ActivityScope
    @Provides
    HospitalGoodsListAdapter provideGoodsAdapter(List<Goods> list) {
        HospitalGoodsListAdapter orderCenterListAdapter = new HospitalGoodsListAdapter(list);
        return orderCenterListAdapter;
    }

    // 第三个页面
    @ActivityScope
    @Provides
    List<DoctorBean> provideDoctorList() {
        ArrayList<DoctorBean> orderBeans = new ArrayList<>();
        return orderBeans;
    }
    @ActivityScope
    @Provides
    DoctorListAdapter provideDoctorAdapter(List<DoctorBean> list) {
        DoctorListAdapter orderCenterListAdapter = new DoctorListAdapter(list);
        return orderCenterListAdapter;
    }

    // 第四个页面
    @ActivityScope
    @Provides
    List<String> provideEnvList() {
        ArrayList<String> orderBeans = new ArrayList<>();
        return orderBeans;
    }
    @ActivityScope
    @Provides
    HospitalEnvImageAdapter provideStoreAdapter(List<String> list) {
        HospitalEnvImageAdapter orderCenterListAdapter = new HospitalEnvImageAdapter(list);
        return orderCenterListAdapter;
    }
}