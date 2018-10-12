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
import me.jessyan.mvparms.demo.mvp.contract.MyMealContract;
import me.jessyan.mvparms.demo.mvp.model.entity.order.Order;
import me.jessyan.mvparms.demo.mvp.model.entity.order.request.OrderOperationRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.AppointmentAndMealRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.ModifyAppointmentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.MyMealResponse;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyMealListAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class MyMealPresenter extends BasePresenter<MyMealContract.Model, MyMealContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    List<Order> appointments;
    @Inject
    MyMealListAdapter mAdapter;

    private int preEndIndex;
    private int lastPageIndex = 1;

    @Inject
    public MyMealPresenter(MyMealContract.Model model, MyMealContract.View rootView) {
        super(model, rootView);
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onCreate() {
        getMyMealAppointment(true);
    }


    public void getMyMealAppointment(boolean pullToRefresh) {
        AppointmentAndMealRequest request = new AppointmentAndMealRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        int type = 0;
        if (null != mRootView.getCache().get("type")) {
            type = (int) mRootView.getCache().get("type");
        }
        switch (type) {
            case 0:
                break;
            case 1:
                request.setOrderStatus("1");
                break;
            case 2:
                request.setOrderStatus("31");
                break;
            case 3:
                request.setOrderStatus("5");
                break;
        }


        if (pullToRefresh) lastPageIndex = 1;
        request.setPageIndex(lastPageIndex);//下拉刷新默认只请求第一页

        mModel.getMyMealAppointment(request)
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
                .subscribe(new ErrorHandleSubscriber<MyMealResponse>(mErrorHandler) {
                    @Override
                    public void onNext(MyMealResponse response) {
                        if (response.isSuccess()) {
                            mRootView.showConent(response.getOrderList().size() > 0);
                            if (pullToRefresh) {
                                appointments.clear();
                            }
                            mRootView.setLoadedAllItems(response.getNextPageIndex() == -1);
                            appointments.addAll(response.getOrderList());
                            preEndIndex = appointments.size();//更新之前列表总长度,用于确定加载更多的起始位置
                            lastPageIndex = appointments.size() / 10 + 1;
                            if (pullToRefresh) {
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mAdapter.notifyItemRangeInserted(preEndIndex, appointments.size());
                            }
                        }
                    }
                });
    }


    public void modifyAppointmentTime() {
        ModifyAppointmentRequest request = new ModifyAppointmentRequest();
        request.setCmd(2108);
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        request.setReservationId((String) mRootView.getCache().get("reservationId"));
        mModel.modifyAppointmentTime(request)
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
                            getMyMealAppointment(false);
                        }
                    }
                });
    }


    public void cancelOrder() {
        OrderOperationRequest request = new OrderOperationRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        request.setOrderId((String) mRootView.getCache().get("orderId"));
        request.setCmd(553);
        mModel.cancelOrder(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.isSuccess()) {
                            getMyMealAppointment(true);
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
