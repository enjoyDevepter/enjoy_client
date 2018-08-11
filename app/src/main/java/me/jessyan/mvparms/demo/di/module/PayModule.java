package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.PayContract;
import me.jessyan.mvparms.demo.mvp.model.PayModel;
import me.jessyan.mvparms.demo.mvp.model.entity.response.PayOrderResponse;
import me.jessyan.mvparms.demo.mvp.ui.adapter.PayGoodsListAdapter;


@Module
public class PayModule {
    private PayContract.View view;

    /**
     * 构建PayModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public PayModule(PayContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    PayContract.View providePayView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    PayContract.Model providePayModel(PayModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @ActivityScope
    @Provides
    List<PayOrderResponse.Goods> provideGoodsList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    RecyclerView.Adapter provideOrderConfirmAdapter(List<PayOrderResponse.Goods> goodsList) {
        return new PayGoodsListAdapter(goodsList);
    }
}