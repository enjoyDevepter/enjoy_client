package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.RecommenderContract;
import me.jessyan.mvparms.demo.mvp.model.RecommenderModel;


@Module
public class RecommenderModule {
    private RecommenderContract.View view;

    /**
     * 构建RecommenderModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public RecommenderModule(RecommenderContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    RecommenderContract.View provideRecommenderView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    RecommenderContract.Model provideRecommenderModel(RecommenderModel model) {
        return model;
    }
}