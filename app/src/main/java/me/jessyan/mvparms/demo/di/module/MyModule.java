package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.MyContract;
import me.jessyan.mvparms.demo.mvp.model.MyModel;


@Module
public class MyModule {
    private MyContract.View view;

    /**
     * 构建MyModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MyModule(MyContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MyContract.View provideMyView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MyContract.Model provideMyModel(MyModel model) {
        return model;
    }
}