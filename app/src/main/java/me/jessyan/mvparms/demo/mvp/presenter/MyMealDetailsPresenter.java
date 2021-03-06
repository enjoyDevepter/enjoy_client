package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;

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
import me.jessyan.mvparms.demo.mvp.contract.MyMealDetailsContract;
import me.jessyan.mvparms.demo.mvp.model.entity.appointment.Appointment;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.ModifyAppointmentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.MyMealDetailRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.ShareRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.AppointmentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryApplyResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.ShareResponse;
import me.jessyan.mvparms.demo.mvp.ui.activity.PlatformActivity;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyMealDetailListAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class MyMealDetailsPresenter extends BasePresenter<MyMealDetailsContract.Model, MyMealDetailsContract.View> {
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
    MyMealDetailListAdapter mAdapter;
    private int preEndIndex;
    private int lastPageIndex = 1;

    @Inject
    public MyMealDetailsPresenter(MyMealDetailsContract.Model model, MyMealDetailsContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onCreate() {
        getAppointment(true);
    }

    public void getAppointment(boolean pullToRefresh) {
        MyMealDetailRequest request = new MyMealDetailRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        request.setOrderId(mRootView.getActivity().getIntent().getStringExtra("orderId"));
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
                            if (pullToRefresh) {
                                appointments.clear();
                            }
                            mRootView.showError(response.getOrderProjectDetailList().size() > 0);
                            mRootView.setLoadedAllItems(response.getNextPageIndex() == -1);
                            appointments.addAll(response.getOrderProjectDetailList());
                            preEndIndex = appointments.size();//更新之前列表总长度,用于确定加载更多的起始位置
                            lastPageIndex = appointments.size() / 10 + 1;
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
     * 查看奖励规则
     */
    public void apply() {

        DiaryRequest request = new DiaryRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        request.setCmd(825);
        mModel.apply(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<DiaryApplyResponse>(mErrorHandler) {
                    @Override
                    public void onNext(DiaryApplyResponse response) {
                        if (response.isSuccess()) {
                            Intent applyIntent = new Intent(mRootView.getActivity(), PlatformActivity.class);
                            applyIntent.putExtra("url", response.getUrl());
                            applyIntent.putExtra("orderId", mRootView.getActivity().getIntent().getStringExtra("orderId"));
                            applyIntent.putExtra("apply", "apply");
                            applyIntent.putExtra("merchId", (String) mRootView.getCache().get("merchId"));
                            applyIntent.putExtra("goodsId", (String) mRootView.getCache().get("goodsId"));
                            ArmsUtils.startActivity(applyIntent);
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
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();//显示下拉刷新的进度条
                }).doFinally(() -> {
            mRootView.hideLoading();//隐藏下拉刷新的进度条
        })
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.isSuccess()) {
                            getAppointment(true);
                        }
                    }
                });
    }


    /**
     * 分享
     */
    public void share() {
        ShareRequest request = new ShareRequest();
        request.setCmd(921);
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        request.setProjectId((String) (mRootView.getCache().get("projectId")));
        mModel.share(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();//显示下拉刷新的进度条
                }).doFinally(() -> {
            mRootView.hideLoading();//隐藏下拉刷新的进度条
        })
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<ShareResponse>(mErrorHandler) {
                    @Override
                    public void onNext(ShareResponse response) {
                        if (response.isSuccess()) {
                            mRootView.share(response.getShare());
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
