package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;

import org.simple.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.mvp.contract.ChoiceTimeContract;
import me.jessyan.mvparms.demo.mvp.model.HAppointments;
import me.jessyan.mvparms.demo.mvp.model.entity.HAppointmentsTime;
import me.jessyan.mvparms.demo.mvp.model.entity.request.GetAppointmentTimeRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.ModifyAppointmentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.GetAppointmentTimeResponse;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DateAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.TimeAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


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
        String type = mRootView.getActivity().getIntent().getStringExtra("type");
        if ("choice_time".equals(type)) {
            appointments.addAll(mRootView.getActivity().getIntent().getParcelableArrayListExtra("appointmnetInfo"));
            appointments.get(0).setChoice(true);
            timeList.addAll(appointments.get(0).getReservationTimeList());
            timeAdapter.notifyDataSetChanged();
            dateAdapter.notifyDataSetChanged();
        } else {
            getAppointmentTime();
        }
    }

    private void getAppointmentTime() {
        GetAppointmentTimeRequest request = new GetAppointmentTimeRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        request.setProjectId(mRootView.getActivity().getIntent().getStringExtra("projectId"));
        mModel.getAppointmentTime(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();//显示下拉刷新的进度条
                }).doFinally(() -> {
            mRootView.hideLoading();//隐藏下拉刷新的进度条
        }).retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<GetAppointmentTimeResponse>(mErrorHandler) {
                    @Override
                    public void onNext(GetAppointmentTimeResponse response) {
                        if (response.isSuccess()) {
                            appointments.clear();
                            appointments.addAll(response.getReservationDateList());
                            appointments.get(0).setChoice(true);
                            timeList.addAll(appointments.get(0).getReservationTimeList());
                            timeAdapter.notifyDataSetChanged();
                            dateAdapter.notifyDataSetChanged();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    public void modifyAppointmentTime() {
        ModifyAppointmentRequest request = new ModifyAppointmentRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        String type = mRootView.getActivity().getIntent().getStringExtra("type");
        if ("add_appointment_time".equals(type)) {
            request.setCmd(2106);
            request.setProjectId(mRootView.getActivity().getIntent().getStringExtra("projectId"));
        } else if ("modify_appointment_time".equals(type)) {
            request.setCmd(2107);
            request.setReservationId(mRootView.getActivity().getIntent().getStringExtra("reservationId"));
        }
        request.setReservationDate((String) mRootView.getCache().get("appointmentsDate"));
        request.setReservationTime((String) mRootView.getCache().get("appointmentsTime"));
        mModel.modifyAppointmentTime(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();//显示下拉刷新的进度条
                }).doFinally(() -> {
            mRootView.hideLoading();//隐藏下拉刷新的进度条
        }).retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.isSuccess()) {
                            EventBus.getDefault().post(mRootView.getActivity().getIntent().getIntExtra("index", 0), EventBusTags.CHANGE_APPOINTMENT_TIME);
                            mRootView.killMyself();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
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
