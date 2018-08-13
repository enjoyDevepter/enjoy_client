package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.DiaryImageContract;
import me.jessyan.mvparms.demo.mvp.model.DiaryImageModel;
import me.jessyan.mvparms.demo.mvp.model.entity.DiaryAlbum;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DiaryImageAdapter;


@Module
public class DiaryImageModule {
    private DiaryImageContract.View view;

    /**
     * 构建DiaryImageModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public DiaryImageModule(DiaryImageContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    DiaryImageContract.View provideDiaryImageView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    DiaryImageContract.Model provideDiaryImageModel(DiaryImageModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new GridLayoutManager(view.getActivity(), 2);
    }

    @ActivityScope
    @Provides
    List<DiaryAlbum> provideDiaryImageList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    DiaryImageAdapter provideDiaryImagesAdapter(List<DiaryAlbum> albums) {
        return new DiaryImageAdapter(albums);
    }

}