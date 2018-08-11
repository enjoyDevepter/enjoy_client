package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.HospitalInfoContract;
import me.jessyan.mvparms.demo.mvp.model.HospitalInfoModel;


@Module
public class HospitalInfoModule {
    private HospitalInfoContract.View view;

    /**
     * 构建HospitalInfoModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public HospitalInfoModule(HospitalInfoContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    HospitalInfoContract.View provideHospitalInfoView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    HospitalInfoContract.Model provideHospitalInfoModel(HospitalInfoModel model) {
        return model;
    }
}