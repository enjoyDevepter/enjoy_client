package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.GetCashListContract;
import me.jessyan.mvparms.demo.mvp.model.GetCashListModel;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.DrawCashBean;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GetCashListAdapter;


@Module
public class GetCashListModule {
    private GetCashListContract.View view;

    /**
     * 构建GetCashListModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public GetCashListModule(GetCashListContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    GetCashListContract.View provideGetCashListView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    GetCashListContract.Model provideGetCashListModel(GetCashListModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.Adapter provideStoreAdapter(List<DrawCashBean> list) {
        return new GetCashListAdapter(list);
    }


    @ActivityScope
    @Provides
    List<DrawCashBean> provideOrderBeanList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

}