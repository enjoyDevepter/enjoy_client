package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.AppointmentContract;
import me.jessyan.mvparms.demo.mvp.model.AppointmentModel;
import me.jessyan.mvparms.demo.mvp.model.entity.appointment.Appointment;
import me.jessyan.mvparms.demo.mvp.ui.adapter.AppointmentListAdapter;


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

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @ActivityScope
    @Provides
    List<Appointment> provideUserList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    AppointmentListAdapter provideUserAdapter(List<Appointment> list) {
        return new AppointmentListAdapter(list);
    }
}