package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.CashCoinContract;
import me.jessyan.mvparms.demo.mvp.model.CashCoinModel;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.CashBean;
import me.jessyan.mvparms.demo.mvp.ui.adapter.CashCoinItemAdapter;


@Module
public class CashCoinModule {
    private CashCoinContract.View view;

    /**
     * 构建CashCoinModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public CashCoinModule(CashCoinContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    CashCoinContract.View provideCashCoinView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    CashCoinContract.Model provideCashCoinModel(CashCoinModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.Adapter provideStoreAdapter(List<CashBean> list) {
        return new CashCoinItemAdapter(list);
    }


    @ActivityScope
    @Provides
    List<CashBean> provideOrderBeanList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

}