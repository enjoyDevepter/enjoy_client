package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import me.jessyan.mvparms.demo.mvp.contract.ChoiceTimeContract;
import me.jessyan.mvparms.demo.mvp.model.HAppointments;
import me.jessyan.mvparms.demo.mvp.model.entity.HAppointmentsTime;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DateAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.TimeAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class ChoiceTimePresenter extends BasePresenter<ChoiceTimeContract.Model, ChoiceTimeContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    List<HAppointments> appointments;
    @Inject
    DateAdapter dateAdapter;
    @Inject
    List<HAppointmentsTime> timeList;
    @Inject
    TimeAdapter timeAdapter;

    @Inject
    public ChoiceTimePresenter(ChoiceTimeContract.Model model, ChoiceTimeContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        appointments.addAll(mRootView.getActivity().getIntent().getParcelableArrayListExtra("appointmnetInfo"));
        timeList.addAll(appointments.get(0).getAppointmentsTimeList());
        timeAdapter.notifyDataSetChanged();
        dateAdapter.notifyDataSetChanged();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }
}
