package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.StoreContract;
import me.jessyan.mvparms.demo.mvp.model.StoreModel;
import me.jessyan.mvparms.demo.mvp.model.entity.Store;
import me.jessyan.mvparms.demo.mvp.ui.adapter.StoreListAdapter;


@Module
public class StoreModule {
    private StoreContract.View view;

    /**
     * 构建StoreModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public StoreModule(StoreContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    StoreContract.View provideStoreView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    StoreContract.Model provideStoreModel(StoreModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @ActivityScope
    @Provides
    List<Store> provideDiariesList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    StoreListAdapter provideGoodsListAdapter(List<Store> diaries) {
        return new StoreListAdapter(diaries);
    }
}