package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.utils.ArmsUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.contract.SearchContract;
import me.jessyan.mvparms.demo.mvp.model.SearchModel;
import me.jessyan.mvparms.demo.mvp.model.entity.Category;
import me.jessyan.mvparms.demo.mvp.ui.adapter.HistoryAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.SearchTypeAdapter;


@Module
public class SearchModule {
    private SearchContract.View view;

    /**
     * 构建SearchModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public SearchModule(SearchContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    SearchContract.View provideSearchView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    SearchContract.Model provideSearchModel(SearchModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    TagAdapter provideSerachAdapter(List<String> list) {
        return new TagAdapter<String>(list) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(ArmsUtils.getContext()).inflate(R.layout.search_hot_item, null, false);
                tv.setText(s);
                return tv;
            }
        };
    }

    @ActivityScope
    @Provides
    List<String> provideHotList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    List<Category> provideCategoryList() {
        return new ArrayList<>();
    }


    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity());
    }

    @ActivityScope
    @Provides
    RecyclerView.Adapter provideHistoryAdapter(List<String> histories) {
        return new HistoryAdapter(histories);
    }

    @ActivityScope
    @Provides
    SearchTypeAdapter provideCodeAdapter(List<Category> list) {
        return new SearchTypeAdapter(list);
    }
}