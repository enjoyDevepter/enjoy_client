package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.TaoCanContract;
import me.jessyan.mvparms.demo.mvp.model.TaoCanModel;
import me.jessyan.mvparms.demo.mvp.model.entity.MealGoods;
import me.jessyan.mvparms.demo.mvp.ui.adapter.TaoCanListAdapter;


@Module
public class TaoCanModule {
    private TaoCanContract.View view;

    /**
     * 构建TaoCanModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public TaoCanModule(TaoCanContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    TaoCanContract.View provideTaoCanView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    TaoCanContract.Model provideTaoCanModel(TaoCanModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @ActivityScope
    @Provides
    List<MealGoods> provideGoodsList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    TaoCanListAdapter provideGoodsListAdapter(List<MealGoods> goods) {
        return new TaoCanListAdapter(goods);
    }
}