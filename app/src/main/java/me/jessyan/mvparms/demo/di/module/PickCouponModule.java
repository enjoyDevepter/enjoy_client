package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.PickCouponContract;
import me.jessyan.mvparms.demo.mvp.model.PickCouponModel;
import me.jessyan.mvparms.demo.mvp.model.entity.PickCoupon;
import me.jessyan.mvparms.demo.mvp.ui.adapter.PickCouponListAdapter;


@Module
public class PickCouponModule {
    private PickCouponContract.View view;

    /**
     * 构建PickCouponModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public PickCouponModule(PickCouponContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    PickCouponContract.View providePickCouponView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    PickCouponContract.Model providePickCouponModel(PickCouponModel model) {
        return model;
    }


    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @ActivityScope
    @Provides
    List<PickCoupon> provideCouponList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    PickCouponListAdapter provideCouponAdapter(List<PickCoupon> list) {
        return new PickCouponListAdapter(list);
    }
}