package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.KGoodsDetailsActivityContract;
import me.jessyan.mvparms.demo.mvp.model.KGoodsDetailsActivityModel;
import me.jessyan.mvparms.demo.mvp.model.entity.Diary;
import me.jessyan.mvparms.demo.mvp.model.entity.Promotion;
import me.jessyan.mvparms.demo.mvp.model.entity.response.GoodsDetailsResponse;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DiaryListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsPromotionAdapter;


@Module
public class KGoodsDetailsActivityModule {
    private KGoodsDetailsActivityContract.View view;

    /**
     * 构建KGoodsDetailsActivityModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public KGoodsDetailsActivityModule(KGoodsDetailsActivityContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    KGoodsDetailsActivityContract.View provideKGoodsDetailsActivityView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    KGoodsDetailsActivityContract.Model provideKGoodsDetailsActivityModel(KGoodsDetailsActivityModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RxPermissions provideRxPermissions() {
        return new RxPermissions(view.getActivity());
    }

    @ActivityScope
    @Provides
    GoodsDetailsResponse provideGoodsDetailsResponse() {
        return new GoodsDetailsResponse();
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @ActivityScope
    @Provides
    List<Promotion> providePromotionList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    GoodsPromotionAdapter providePromotionAdapter(List<Promotion> list) {
        return new GoodsPromotionAdapter(list);
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