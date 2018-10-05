package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.TimelimitContract;
import me.jessyan.mvparms.demo.mvp.model.TimelimitModel;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsLimitListAdapter;

@Module
public class TimelimitModule {
    private TimelimitContract.View view;

    /**
     * 构建TimelimitModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public TimelimitModule(TimelimitContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    TimelimitContract.View provideTimelimitView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    TimelimitContract.Model provideTimelimitModel(TimelimitModel model) {
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
    GoodsLimitListAdapter provideGoodsListAdapter(List<Goods> goods) {
        return new GoodsLimitListAdapter(goods);
    }

}