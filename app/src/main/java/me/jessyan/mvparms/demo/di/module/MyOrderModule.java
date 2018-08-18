package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.MyOrderContract;
import me.jessyan.mvparms.demo.mvp.model.MyOrderModel;
import me.jessyan.mvparms.demo.mvp.model.entity.order.Order;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyOrderAdapter;


@Module
public class MyOrderModule {
    private MyOrderContract.View view;

    /**
     * 构建MyOrderModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MyOrderModule(MyOrderContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MyOrderContract.View provideMyOrderView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MyOrderContract.Model provideMyOrderModel(MyOrderModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @ActivityScope
    @Provides
    List<Order> provideUserList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    MyOrderAdapter provideUserAdapter(List<Order> list) {
        return new MyOrderAdapter(list);
    }
}