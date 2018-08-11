package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.TaoCanDetailsContract;
import me.jessyan.mvparms.demo.mvp.model.TaoCanDetailsModel;
import me.jessyan.mvparms.demo.mvp.model.entity.MealGoods;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MealDetailsListAdapter;


@Module
public class TaoCanDetailsModule {
    private TaoCanDetailsContract.View view;

    /**
     * 构建TaoCanDetailsModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public TaoCanDetailsModule(TaoCanDetailsContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    TaoCanDetailsContract.View provideTaoCanDetailsView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    TaoCanDetailsContract.Model provideTaoCanDetailsModel(TaoCanDetailsModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.HORIZONTAL, false);
    }

    @ActivityScope
    @Provides
    List<MealGoods.Goods> provideGoodsList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    RecyclerView.Adapter provideGoodsListAdapter(List<MealGoods.Goods> goods) {
        return new MealDetailsListAdapter(goods);
    }
}