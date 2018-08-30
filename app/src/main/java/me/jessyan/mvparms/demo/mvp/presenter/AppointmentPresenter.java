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

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.AppointmentContract;
import me.jessyan.mvparms.demo.mvp.model.entity.appointment.Appointment;
import me.jessyan.mvparms.demo.mvp.model.entity.request.AppointmentAndMealRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.ModifyAppointmentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.AppointmentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.ui.adapter.AppointmentListAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class AppointmentPresenter extends BasePresenter<AppointmentContract.Model, AppointmentContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    List<Appointment> appointments;
    @Inject
    AppointmentListAdapter mAdapter;

    private int preEndIndex;
    private int lastPageIndex = 1;


    @Inject
    public AppointmentPresenter(AppointmentContract.Model model, AppointmentContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void initAppointment() {
        getAppointment(true);
    }

    public void getAppointment(boolean pullToRefresh) {
        AppointmentAndMealRequest request = new AppointmentAndMealRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        if (ArmsUtils.isEmpty(request.getToken())) {
            return;
        }
        int type = 0;
        if (null != mRootView.getCache().get("type")) {
            type = (int) mRootView.getCache().get("type");
        }
        switch (type) {
            case 0:
                mRootView.showError(false);
                return;
            case 1:
                switch (((int) mRootView.getCache().get("status"))) {
                    case 0:
                        request.setCmd(2000);
                        break;
                    case 1:
                        request.setCmd(2001);
                        break;
                    case 2:
                        request.setCmd(2002);
                        break;
                }
                break;
            case 2:
                mRootView.showError(false);
                return;
        }


        if (pullToRefresh) lastPageIndex = 1;
        request.setPageIndex(lastPageIndex);//下拉刷新默认只请求第一页

        mModel.getAppointment(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    if (pullToRefresh)
                        mRootView.showLoading();//显示下拉刷新的进度条
                    else
                        mRootView.startLoadMore();//显示上拉加载更多的进度条
                })
                .doFinally(() -> {
                    if (pullToRefresh)
                        mRootView.hideLoading();//隐藏下拉刷新的进度条
                    else
                        mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                })
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<AppointmentResponse>(mErrorHandler) {
                    @Override
                    public void onNext(AppointmentResponse response) {
                        if (response.isSuccess()) {
                            mRootView.showError(response.getOrderProjectDetailList().size() > 0);
                            if (pullToRefresh) {
                                appointments.clear();
                            }
                            mRootView.setLoadedAllItems(response.getNextPageIndex() == -1);
                            appointments.addAll(response.getOrderProjectDetailList());
                            preEndIndex = appointments.size();//更新之前列表总长度,用于确定加载更多的起始位置
                            lastPageIndex = appointments.size() / 10;
                            if (pullToRefresh) {
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mAdapter.notifyItemRangeInserted(preEndIndex, appointments.size());
                            }
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    /**
     * 取消预约
     */
    public void cancelAppointment() {
        ModifyAppointmentRequest request = new ModifyAppointmentRequest();
        request.setCmd(2108);
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        request.setReservationId((String) mRootView.getCache().get("reservationId"));
        mModel.cancelAppointment(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();//显示下拉刷新的进度条
                }).doFinally(() -> {
            mRootView.hideLoading();//隐藏下拉刷新的进度条

        }).subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
            @Override
            public void onNext(BaseResponse response) {
                if (response.isSuccess()) {
                    getAppointment(true);
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
