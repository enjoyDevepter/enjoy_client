package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.NewlywedsContract;
import me.jessyan.mvparms.demo.mvp.model.NewlywedsModel;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsNewlyListAdapter;


@Module
public class NewlywedsModule {
    private NewlywedsContract.View view;

    /**
     * 构建NewlywedsModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public NewlywedsModule(NewlywedsContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    NewlywedsContract.View provideNewlywedsView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    NewlywedsContract.Model provideNewlywedsModel(NewlywedsModel model) {
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
    GoodsNewlyListAdapter provideGoodsListAdapter(List<Goods> goods) {
        return new GoodsNewlyListAdapter(goods);
    }
}

