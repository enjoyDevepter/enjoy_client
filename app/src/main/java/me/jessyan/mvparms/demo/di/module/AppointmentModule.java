package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.AppointmentContract;
import me.jessyan.mvparms.demo.mvp.model.AppointmentModel;


@Module
public class AppointmentModule {
    private AppointmentContract.View view;

    /**
     * 构建AppointmentModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public AppointmentModule(AppointmentContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    AppointmentContract.View provideAppointmentView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    AppointmentContract.Model provideAppointmentModel(AppointmentModel model) {
        return model;
    }
}