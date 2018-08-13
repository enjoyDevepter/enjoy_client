package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.DiaryForGoodsContract;
import me.jessyan.mvparms.demo.mvp.model.DiaryForGoodsModel;
import me.jessyan.mvparms.demo.mvp.model.entity.Diary;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyDiaryListAdapter;


@Module
public class DiaryForGoodsModule {
    private DiaryForGoodsContract.View view;

    /**
     * 构建DiaryForGoodsModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public DiaryForGoodsModule(DiaryForGoodsContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    DiaryForGoodsContract.View provideDiaryForGoodsView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    DiaryForGoodsContract.Model provideDiaryForGoodsModel(DiaryForGoodsModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @ActivityScope
    @Provides
    List<Diary> provideDiariesList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    MyDiaryListAdapter provideGoodsListAdapter(List<Diary> diaries) {
        return new MyDiaryListAdapter(diaries);
    }

}