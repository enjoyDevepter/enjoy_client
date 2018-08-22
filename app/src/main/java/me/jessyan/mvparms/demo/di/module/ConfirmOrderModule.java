package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.ConfirmOrderContract;
import me.jessyan.mvparms.demo.mvp.model.ConfirmOrderModel;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.ui.adapter.OrderConfirmGoodsListAdapter;


@Module
public class ConfirmOrderModule {
    private ConfirmOrderContract.View view;

    /**
     * 构建ConfirmOrderModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ConfirmOrderModule(ConfirmOrderContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ConfirmOrderContract.View provideConfirmOrderView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ConfirmOrderContract.Model provideConfirmOrderModel(ConfirmOrderModel model) {
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
    RecyclerView.Adapter provideOrderConfirmAdapter(List<Goods> list) {
        return new OrderConfirmGoodsListAdapter(list);
    }
}