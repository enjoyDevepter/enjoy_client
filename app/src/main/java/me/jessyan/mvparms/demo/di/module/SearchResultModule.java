package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.SearchResultContract;
import me.jessyan.mvparms.demo.mvp.model.SearchResultModel;
import me.jessyan.mvparms.demo.mvp.model.entity.Category;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.model.entity.HGoods;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsFilterSecondAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.HGoodsListAdapter;


@Module
public class SearchResultModule {
    private SearchResultContract.View view;

    /**
     * 构建SerachResultModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public SearchResultModule(SearchResultContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    SearchResultContract.View provideSerachResultView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    SearchResultContract.Model provideSerachResultModel(SearchResultModel model) {
        return model;
    }


    @ActivityScope
    @Provides
    RxPermissions provideRxPermissions() {
        return new RxPermissions(view.getActivity());
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @ActivityScope
    @Provides
    List<Goods> provideGoodsList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    List<HGoods> provideHGoodsList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    List<Category> provideCategories() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    GoodsListAdapter provideGoodsListAdapter(List<Goods> goods) {
        return new GoodsListAdapter(goods);
    }

    @ActivityScope
    @Provides
    HGoodsListAdapter provideHGoodsListAdapter(List<HGoods> goods) {
        return new HGoodsListAdapter(goods);
    }


    @ActivityScope
    @Provides
    GoodsFilterSecondAdapter provideFilterSecondAdapter(List<Category> categories) {
        return new GoodsFilterSecondAdapter(categories);
    }

}