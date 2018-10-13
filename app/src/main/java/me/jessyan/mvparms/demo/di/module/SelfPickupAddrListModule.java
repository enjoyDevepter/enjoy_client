package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.SelfPickupAddrListContract;
import me.jessyan.mvparms.demo.mvp.model.SelfPickupAddrListModel;
import me.jessyan.mvparms.demo.mvp.model.entity.AreaAddress;
import me.jessyan.mvparms.demo.mvp.model.entity.CommonStoreDateType;
import me.jessyan.mvparms.demo.mvp.ui.adapter.StoresListAdapter;


@Module
public class SelfPickupAddrListModule {
    private SelfPickupAddrListContract.View view;

    /**
     * 构建SelfPickupAddrListModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public SelfPickupAddrListModule(SelfPickupAddrListContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    SelfPickupAddrListContract.View provideSelfPickupAddrListView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    SelfPickupAddrListContract.Model provideSelfPickupAddrListModel(SelfPickupAddrListModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    List<AreaAddress> provideAddressList() {
        return new ArrayList<>();
    }


    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @ActivityScope
    @Provides
    List<CommonStoreDateType> provideStoreList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    StoresListAdapter provideStoreAdapter(List<CommonStoreDateType> list) {
        return new StoresListAdapter(list);
    }
}