package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.MyDiaryContract;
import me.jessyan.mvparms.demo.mvp.model.MyDiaryModel;
import me.jessyan.mvparms.demo.mvp.model.entity.Diary;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DiaryListAdapter;


@Module
public class MyDiaryModule {
    private MyDiaryContract.View view;

    /**
     * 构建MyDiaryModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MyDiaryModule(MyDiaryContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MyDiaryContract.View provideMyDiaryView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MyDiaryContract.Model provideMyDiaryModel(MyDiaryModel model) {
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
    DiaryListAdapter provideGoodsListAdapter(List<Diary> diaries) {
        return new DiaryListAdapter(diaries);
    }
}