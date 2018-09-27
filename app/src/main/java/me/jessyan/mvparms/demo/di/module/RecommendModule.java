package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.RecommendContract;
import me.jessyan.mvparms.demo.mvp.model.RecommendModel;
import me.jessyan.mvparms.demo.mvp.model.entity.Category;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.model.entity.HGoods;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsFilterSecondAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.HGoodsListAdapter;


@Module
public class RecommendModule {
    private RecommendContract.View view;

    /**
     * 构建RecommendModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public RecommendModule(RecommendContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    RecommendContract.View provideRecommendView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    RecommendContract.Model provideRecommendModel(RecommendModel model) {
        return model;
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
    GoodsFilterSecondAdapter provideFilterSecondAdapter(List<Category> categories) {
        return new GoodsFilterSecondAdapter(categories);
    }

    @ActivityScope
    @Provides
    List<HGoods> provideHGoodsList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    HGoodsListAdapter provideHGoodsListAdapter(List<HGoods> goods) {
        return new HGoodsListAdapter(goods);
    }

}