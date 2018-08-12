package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.HospitalChooseContract;
import me.jessyan.mvparms.demo.mvp.model.HospitalChooseModel;


@Module
public class HospitalChooseModule {
    private HospitalChooseContract.View view;

    /**
     * 构建HospitalChooseModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public HospitalChooseModule(HospitalChooseContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    HospitalChooseContract.View provideHospitalChooseView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    HospitalChooseContract.Model provideHospitalChooseModel(HospitalChooseModel model) {
        return model;
    }
}