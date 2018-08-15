package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.ChoiceTimeContract;
import me.jessyan.mvparms.demo.mvp.model.ChoiceTimeModel;
import me.jessyan.mvparms.demo.mvp.model.HAppointments;
import me.jessyan.mvparms.demo.mvp.model.entity.HAppointmentsTime;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DateAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.TimeAdapter;


@Module
public class ChoiceTimeModule {
    private ChoiceTimeContract.View view;

    /**
     * 构建ChoiceTimeModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ChoiceTimeModule(ChoiceTimeContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ChoiceTimeContract.View provideChoiceTimeView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ChoiceTimeContract.Model provideChoiceTimeModel(ChoiceTimeModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @ActivityScope
    @Provides
    List<HAppointments> provideAppointmentList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    DateAdapter provideDateAdapter(List<HAppointments> list) {
        return new DateAdapter(list);
    }

    @ActivityScope
    @Provides
    TimeAdapter provideTimeAdapter(List<HAppointmentsTime> list) {
        return new TimeAdapter(list);
    }

    @ActivityScope
    @Provides
    List<HAppointmentsTime> provideAppointmenTimetList() {
        return new ArrayList<>();
    }

}