package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.UserIntegralContract;
import me.jessyan.mvparms.demo.mvp.model.UserIntegralModel;
import me.jessyan.mvparms.demo.mvp.model.entity.score.ScorePointBean;
import me.jessyan.mvparms.demo.mvp.ui.adapter.UserScoreAdapter;


@Module
public class UserIntegralModule {
    private UserIntegralContract.View view;

    /**
     * 构建UserIntegralModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public UserIntegralModule(UserIntegralContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    UserIntegralContract.View provideUserIntegralView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    UserIntegralContract.Model provideUserIntegralModel(UserIntegralModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.Adapter provideStoreAdapter(List<ScorePointBean> list) {
        return new UserScoreAdapter(list);
    }


    @ActivityScope
    @Provides
    List<ScorePointBean> provideOrderBeanList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }
}