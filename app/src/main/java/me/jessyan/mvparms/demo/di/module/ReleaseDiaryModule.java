package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.ReleaseDiaryContract;
import me.jessyan.mvparms.demo.mvp.model.ReleaseDiaryModel;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DiaryUpdateImageAdapter;


@Module
public class ReleaseDiaryModule {
    private ReleaseDiaryContract.View view;

    /**
     * 构建ReleaseDiaryModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ReleaseDiaryModule(ReleaseDiaryContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ReleaseDiaryContract.View provideReleaseDiaryView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ReleaseDiaryContract.Model provideReleaseDiaryModel(ReleaseDiaryModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RxPermissions provideRxPermissions() {
        return new RxPermissions(view.getActivity());
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.HORIZONTAL, false);
    }

    @ActivityScope
    @Provides
    List<String> provideDiariesList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    RecyclerView.Adapter provideGoodsListAdapter(List<String> images) {
        return new DiaryUpdateImageAdapter(images);
    }


}