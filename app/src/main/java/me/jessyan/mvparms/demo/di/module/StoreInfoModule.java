package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.StoreInfoContract;
import me.jessyan.mvparms.demo.mvp.model.StoreInfoModel;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.HospitalEnvImageAdapter;


@Module
public class StoreInfoModule {
    private StoreInfoContract.View view;

    /**
     * 构建StoreInfoModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public StoreInfoModule(StoreInfoContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    StoreInfoContract.View provideStoreInfoView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    StoreInfoContract.Model provideStoreInfoModel(StoreInfoModel model) {
        return model;
    }

    // 第二个页面
    @ActivityScope
    @Provides
    List<Goods> provideGoodsList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    GoodsListAdapter provideGoodsAdapter(List<Goods> list) {
        return new GoodsListAdapter(list);
    }

    // 第三个页面
    @ActivityScope
    @Provides
    List<String> provideEnvList() {
        ArrayList<String> orderBeans = new ArrayList<>();
        return orderBeans;
    }

    @ActivityScope
    @Provides
    HospitalEnvImageAdapter provideStoreAdapter(List<String> list) {
        return new HospitalEnvImageAdapter(list);
    }
}