package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.ChoiceStoreContract;
import me.jessyan.mvparms.demo.mvp.model.ChoiceStoreModel;
import me.jessyan.mvparms.demo.mvp.model.entity.CommonStoreDateType;
import me.jessyan.mvparms.demo.mvp.ui.adapter.StoresListAdapter;


@Module
public class ChoiceStoreModule {
    private ChoiceStoreContract.View view;

    /**
     * 构建ChoiceStoreModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ChoiceStoreModule(ChoiceStoreContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ChoiceStoreContract.View provideChoiceStoreView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ChoiceStoreContract.Model provideChoiceStoreModel(ChoiceStoreModel model) {
        return model;
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
    RecyclerView.Adapter provideStoreAdapter(List<CommonStoreDateType> list) {
        return new StoresListAdapter(list);
    }
}