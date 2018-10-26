package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.KGoodsOrderConfirmContract;
import me.jessyan.mvparms.demo.mvp.model.KGoodsOrderConfirmModel;


@Module
public class KGoodsOrderConfirmModule {
    private KGoodsOrderConfirmContract.View view;

    /**
     * 构建KGoodsOrderConfirmModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public KGoodsOrderConfirmModule(KGoodsOrderConfirmContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    KGoodsOrderConfirmContract.View provideKGoodsOrderConfirmView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    KGoodsOrderConfirmContract.Model provideKGoodsOrderConfirmModel(KGoodsOrderConfirmModel model) {
        return model;
    }
}