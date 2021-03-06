package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.ConsumeCoinContract;
import me.jessyan.mvparms.demo.mvp.model.ConsumeCoinModel;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.BalanceBean;
import me.jessyan.mvparms.demo.mvp.ui.adapter.ConsumeCoinAdapter;


@Module
public class ConsumeCoinModule {
    private ConsumeCoinContract.View view;

    /**
     * 构建ConsumeCoinModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ConsumeCoinModule(ConsumeCoinContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ConsumeCoinContract.View provideConsumeCoinView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ConsumeCoinContract.Model provideConsumeCoinModel(ConsumeCoinModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.Adapter provideStoreAdapter(List<BalanceBean> list) {
        return new ConsumeCoinAdapter(list);
    }


    @ActivityScope
    @Provides
    List<BalanceBean> provideOrderBeanList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

}