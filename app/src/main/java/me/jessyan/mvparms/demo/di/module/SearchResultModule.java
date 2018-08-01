package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.SearchResultContract;
import me.jessyan.mvparms.demo.mvp.model.SearchResultModel;


@Module
public class SearchResultModule {
    private SearchResultContract.View view;

    /**
     * 构建SerachResultModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public SearchResultModule(SearchResultContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    SearchResultContract.View provideSerachResultView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    SearchResultContract.Model provideSerachResultModel(SearchResultModel model) {
        return model;
    }
}