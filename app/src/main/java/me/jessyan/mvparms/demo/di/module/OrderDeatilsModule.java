package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.OrderDeatilsContract;
import me.jessyan.mvparms.demo.mvp.model.OrderDeatilsModel;
import me.jessyan.mvparms.demo.mvp.model.entity.order.OrderGoods;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyOrderDetailsAdapter;


@Module
public class OrderDeatilsModule {
    private OrderDeatilsContract.View view;

    /**
     * 构建OrderDeatilsModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public OrderDeatilsModule(OrderDeatilsContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    OrderDeatilsContract.View provideOrderDeatilsView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    OrderDeatilsContract.Model provideOrderDeatilsModel(OrderDeatilsModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @ActivityScope
    @Provides
    List<OrderGoods> provideUserList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    RecyclerView.Adapter provideUserAdapter(List<OrderGoods> list) {
        return new MyOrderDetailsAdapter(list);
    }
}