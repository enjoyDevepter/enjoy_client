package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.MyFarvirateContract;
import me.jessyan.mvparms.demo.mvp.model.MyFarvirateModel;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.model.entity.MealGoods;
import me.jessyan.mvparms.demo.mvp.ui.adapter.FarvirateTaoCanListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyFarvirateGoodsListAdapter;


@Module
public class MyFarvirateModule {
    private MyFarvirateContract.View view;

    /**
     * 构建MyFarvirateModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MyFarvirateModule(MyFarvirateContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MyFarvirateContract.View provideMyFarvirateView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MyFarvirateContract.Model provideMyFarvirateModel(MyFarvirateModel model) {
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
    MyFarvirateGoodsListAdapter provideGoodsListAdapter(List<Goods> goods) {
        return new MyFarvirateGoodsListAdapter(goods);
    }

    @ActivityScope
    @Provides
    List<MealGoods> provideMealGoodsList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    FarvirateTaoCanListAdapter provideMealGoodsListAdapter(List<MealGoods> goods) {
        return new FarvirateTaoCanListAdapter(goods);
    }
}