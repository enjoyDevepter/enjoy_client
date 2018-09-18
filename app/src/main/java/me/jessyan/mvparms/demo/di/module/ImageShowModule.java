package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.ImageShowContract;
import me.jessyan.mvparms.demo.mvp.model.ImageShowModel;


@Module
public class ImageShowModule {
    private ImageShowContract.View view;

    /**
     * 构建ImageShowModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ImageShowModule(ImageShowContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ImageShowContract.View provideImageShowView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ImageShowContract.Model provideImageShowModel(ImageShowModel model) {
        return model;
    }
}