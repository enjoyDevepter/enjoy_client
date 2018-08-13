package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.DiaryDetailsContract;
import me.jessyan.mvparms.demo.mvp.model.DiaryDetailsModel;
import me.jessyan.mvparms.demo.mvp.model.entity.DiaryComment;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DiaryCommentListAdapter;


@Module
public class DiaryDetailsModule {
    private DiaryDetailsContract.View view;

    /**
     * 构建DiaryDetailsModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public DiaryDetailsModule(DiaryDetailsContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    DiaryDetailsContract.View provideDiaryDetailsView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    DiaryDetailsContract.Model provideDiaryDetailsModel(DiaryDetailsModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @ActivityScope
    @Provides
    List<DiaryComment> provideDiariyCommentsList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    RecyclerView.Adapter provideGoodsListAdapter(List<DiaryComment> diaries) {
        return new DiaryCommentListAdapter(diaries);
    }


}