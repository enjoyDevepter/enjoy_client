package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.MyMealContract;
import me.jessyan.mvparms.demo.mvp.model.MyMealModel;
import me.jessyan.mvparms.demo.mvp.model.entity.order.Order;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyMealListAdapter;


@Module
public class MyMealModule {
    private MyMealContract.View view;

    /**
     * 构建MyMealModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MyMealModule(MyMealContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MyMealContract.View provideMyMealView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MyMealContract.Model provideMyMealModel(MyMealModel model) {
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
    MyMealListAdapter provideUserAdapter(List<Order> list) {
        return new MyMealListAdapter(list);
    }
}