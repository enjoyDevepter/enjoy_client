package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.MyStoreContract;
import me.jessyan.mvparms.demo.mvp.model.MyStoreModel;
import me.jessyan.mvparms.demo.mvp.model.entity.CommonStoreDateType;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyStoresListAdapter;


@Module
public class MyStoreModule {
    private MyStoreContract.View view;

    /**
     * 构建MyStoreModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MyStoreModule(MyStoreContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MyStoreContract.View provideMyStoreView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MyStoreContract.Model provideMyStoreModel(MyStoreModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @ActivityScope
    @Provides
    List<CommonStoreDateType> provideDiariesList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    MyStoresListAdapter provideHospitalListAdapter(List<CommonStoreDateType> members) {
        return new MyStoresListAdapter(members);
    }
}