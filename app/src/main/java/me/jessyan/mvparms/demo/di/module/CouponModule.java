package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.CouponContract;
import me.jessyan.mvparms.demo.mvp.model.CouponModel;
import me.jessyan.mvparms.demo.mvp.model.entity.Coupon;
import me.jessyan.mvparms.demo.mvp.ui.adapter.CouponListAdapter;


@Module
public class CouponModule {
    private CouponContract.View view;

    /**
     * 构建CouponModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public CouponModule(CouponContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    CouponContract.View provideCouponView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    CouponContract.Model provideCouponModel(CouponModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @ActivityScope
    @Provides
    List<Coupon> provideCouponList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    CouponListAdapter provideCouponAdapter(List<Coupon> list) {
        return new CouponListAdapter(list);
    }
}