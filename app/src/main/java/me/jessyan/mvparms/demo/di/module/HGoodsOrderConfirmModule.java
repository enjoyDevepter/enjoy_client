package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.HGoodsOrderConfirmContract;
import me.jessyan.mvparms.demo.mvp.model.HGoodsOrderConfirmModel;


@Module
public class HGoodsOrderConfirmModule {
    private HGoodsOrderConfirmContract.View view;

    /**
     * 构建HGoodsOrderConfirmModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public HGoodsOrderConfirmModule(HGoodsOrderConfirmContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    HGoodsOrderConfirmContract.View provideHGoodsOrderConfirmView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    HGoodsOrderConfirmContract.Model provideHGoodsOrderConfirmModel(HGoodsOrderConfirmModel model) {
        return model;
    }
}