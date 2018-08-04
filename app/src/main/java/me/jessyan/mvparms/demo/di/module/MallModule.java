package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.MallContract;
import me.jessyan.mvparms.demo.mvp.model.MallModel;
import me.jessyan.mvparms.demo.mvp.model.entity.Category;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsFilterSecondAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsListAdapter;


@Module
public class MallModule {
    private MallContract.View view;

    /**
     * 构建MallModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MallModule(MallContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MallContract.View provideMallView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MallContract.Model provideMallModel(MallModel model) {
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

//    @ActivityScope
//    @Provides
//    GoodsFilterThirdAdapter provideFilterThirdAdapter(List<Category> categories) {
//        return new GoodsFilterThirdAdapter(categories);
//    }

}