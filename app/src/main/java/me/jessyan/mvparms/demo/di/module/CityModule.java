package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.CityContract;
import me.jessyan.mvparms.demo.mvp.model.CityModel;


@Module
public class CityModule {
    private CityContract.View view;

    /**
     * 构建CityModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public CityModule(CityContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    CityContract.View provideCityView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    CityContract.Model provideCityModel(CityModel model) {
        return model;
    }
}