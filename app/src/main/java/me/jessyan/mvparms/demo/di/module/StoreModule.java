package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.StoreContract;
import me.jessyan.mvparms.demo.mvp.model.StoreModel;


@Module
public class StoreModule {
    private StoreContract.View view;

    /**
     * 构建StoreModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public StoreModule(StoreContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    StoreContract.View provideStoreView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    StoreContract.Model provideStoreModel(StoreModel model) {
        return model;
    }
}