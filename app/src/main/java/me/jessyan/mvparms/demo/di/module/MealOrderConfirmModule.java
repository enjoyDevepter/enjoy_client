package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.MealOrderConfirmContract;
import me.jessyan.mvparms.demo.mvp.model.MealOrderConfirmModel;


@Module
public class MealOrderConfirmModule {
    private MealOrderConfirmContract.View view;

    /**
     * 构建MealOrderConfirmModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MealOrderConfirmModule(MealOrderConfirmContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MealOrderConfirmContract.View provideMealOrderConfirmView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MealOrderConfirmContract.Model provideMealOrderConfirmModel(MealOrderConfirmModel model) {
        return model;
    }


}