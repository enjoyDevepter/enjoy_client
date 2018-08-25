package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.AddBankCardContract;
import me.jessyan.mvparms.demo.mvp.model.AddBankCardModel;


@Module
public class AddBankCardModule {
    private AddBankCardContract.View view;

    /**
     * 构建AddBankCardModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public AddBankCardModule(AddBankCardContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    AddBankCardContract.View provideAddBankCardView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    AddBankCardContract.Model provideAddBankCardModel(AddBankCardModel model) {
        return model;
    }
}