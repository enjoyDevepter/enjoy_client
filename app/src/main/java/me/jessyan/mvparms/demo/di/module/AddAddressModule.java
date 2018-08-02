package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.AddAddressContract;
import me.jessyan.mvparms.demo.mvp.model.AddAddressModel;


@Module
public class AddAddressModule {
    private AddAddressContract.View view;

    /**
     * 构建ContactsModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public AddAddressModule(AddAddressContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    AddAddressContract.View provideContactsView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    AddAddressContract.Model provideContactsModel(AddAddressModel model) {
        return model;
    }
}