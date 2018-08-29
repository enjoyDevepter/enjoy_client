package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.ChoiceProjectForDiaryContract;
import me.jessyan.mvparms.demo.mvp.model.ChoiceProjectForDiaryModel;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.ui.adapter.ProjectForDiaryListAdapter;


@Module
public class ChoiceProjectForDiaryModule {
    private ChoiceProjectForDiaryContract.View view;

    /**
     * 构建ChoiceProjectForDiaryModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ChoiceProjectForDiaryModule(ChoiceProjectForDiaryContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ChoiceProjectForDiaryContract.View provideChoiceProjectForDiaryView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ChoiceProjectForDiaryContract.Model provideChoiceProjectForDiaryModel(ChoiceProjectForDiaryModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @ActivityScope
    @Provides
    List<Goods> provideGoodsList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    DefaultAdapter provideProjectAdapter(List<Goods> list) {
        return new ProjectForDiaryListAdapter(list);
    }

}