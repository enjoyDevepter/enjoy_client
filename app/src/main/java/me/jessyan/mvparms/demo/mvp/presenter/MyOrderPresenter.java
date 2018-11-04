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
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.MyOrderContract;
import me.jessyan.mvparms.demo.mvp.model.entity.order.Order;
import me.jessyan.mvparms.demo.mvp.model.entity.order.request.OrderOperationRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.OrderRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryApplyResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.OrderResponse;
import me.jessyan.mvparms.demo.mvp.ui.activity.PlatformActivity;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyOrderAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class MyOrderPresenter extends BasePresenter<MyOrderContract.Model, MyOrderContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    List<Order> orderList;
    @Inject
    MyOrderAdapter mAdapter;

    private int preEndIndex;
    private int lastPageIndex = 1;

    @Inject
    public MyOrderPresenter(MyOrderContract.Model model, MyOrderContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        getOrder(true);
    }

    public void getOrder(boolean pullToRefresh) {
        int type = 0;
        if (null != mRootView.getCache().get("type")) {
            type = (int) mRootView.getCache().get("type");
        }
        switch (type) {
            case 0:
                getHOrder(pullToRefresh);
                break;
            case 1:
                getKOrder(pullToRefresh);
                break;
            case 2:
                getSOrder(pullToRefresh);
                break;
        }
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
                            getOrder(true);
                        }
                    }
                });
    }

    public void reminding() {
        OrderOperationRequest request = new OrderOperationRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        request.setOrderId((String) mRootView.getCache().get("orderId"));
        request.setCmd(555);
        mModel.cancelOrder(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.isSuccess()) {
                            mRootView.showMessage("提醒发货成功");
                        }
                    }
                });
    }


    public void confirmReceipt() {
        OrderOperationRequest request = new OrderOperationRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        request.setOrderId((String) mRootView.getCache().get("orderId"));
        request.setCmd(556);
        mModel.cancelOrder(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.isSuccess()) {
                            getOrder(true);
                        }
                    }
                });
    }

    /**
     * 商美订单
     */
    private void getSOrder(boolean pullToRefresh) {
        OrderRequest request = new OrderRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        request.setCmd(550);
        int statusInt = 0;
        if (null != mRootView.getCache().get("status")) {
            statusInt = (int) mRootView.getCache().get("status");
        }
        String status = "";
//        1,3,4,5
        switch (statusInt) {
            case 0:
                status = "";
                break;
            case 1:
                status = "1";
                break;
            case 2:
                status = "3";
                break;
            case 3:
                status = "4";
                break;
            case 4:
                status = "5";
                break;
        }
        request.setOrderStatus(status);

        if (pullToRefresh) lastPageIndex = 1;
        request.setPageIndex(lastPageIndex);//下拉刷新默认只请求第一页

        mModel.getMyOrder(request)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    if (pullToRefresh)
                        mRootView.showLoading();//显示下拉刷新的进度条
                    else
                        mRootView.startLoadMore();//显示上拉加载更多的进度条
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (pullToRefresh)
                        mRootView.hideLoading();//隐藏下拉刷新的进度条
                    else
                        mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                })
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<OrderResponse>(mErrorHandler) {
                    @Override
                    public void onNext(OrderResponse response) {
                        if (response.isSuccess()) {
                            mRootView.showConent(response.getOrderList().size() > 0);
                            if (pullToRefresh) {
                                orderList.clear();
                            }
                            mRootView.setLoadedAllItems(response.getNextPageIndex() == -1);
                            orderList.addAll(response.getOrderList());
                            preEndIndex = orderList.size();//更新之前列表总长度,用于确定加载更多的起始位置
                            lastPageIndex = orderList.size() / 10 + 1;
                            if (pullToRefresh) {
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mAdapter.notifyItemRangeInserted(preEndIndex, orderList.size());
                            }
                        }
                    }
                });
    }

    /**
     * 医美订单
     */
    private void getHOrder(boolean pullToRefresh) {
        OrderRequest request = new OrderRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        int statusInt = 0;
        if (null != mRootView.getCache().get("status")) {
            statusInt = (int) mRootView.getCache().get("status");
        }
        String status = "";
//        1,2,3,5
        switch (statusInt) {
            case 0:
                status = "";
                break;
            case 1:
                status = "1";
                break;
            case 2:
                status = "2";
                break;
            case 3:
                status = "31";
                break;
            case 4:
                status = "5";
                break;
        }
        request.setOrderStatus(status);
        request.setCmd(570);
        if (pullToRefresh) lastPageIndex = 1;
        request.setPageIndex(lastPageIndex);//下拉刷新默认只请求第一页

        mModel.getMyOrder(request)
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
                }).subscribe(new Consumer<OrderResponse>() {
            @Override
            public void accept(OrderResponse response) throws Exception {
                if (response.isSuccess()) {
                    // 数据转换
                    mRootView.showConent(response.getOrderList().size() > 0);
                    if (pullToRefresh) {
                        orderList.clear();
                    }
                    mRootView.setLoadedAllItems(response.getNextPageIndex() == -1);
                    orderList.addAll(response.getOrderList());
                    preEndIndex = orderList.size();//更新之前列表总长度,用于确定加载更多的起始位置
                    lastPageIndex = orderList.size() / 10;
                    if (pullToRefresh) {
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mAdapter.notifyItemRangeInserted(preEndIndex, orderList.size());
                    }
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
                            applyIntent.putExtra("orderId", (String) mRootView.getCache().get("orderId"));
                            applyIntent.putExtra("apply", "apply");
                            applyIntent.putExtra("merchId", (String) mRootView.getCache().get("merchId"));
                            applyIntent.putExtra("goodsId", (String) mRootView.getCache().get("goodsId"));
                            ArmsUtils.startActivity(applyIntent);
                        }
                    }
                });
    }

    /**
     * 生美订单
     */
    private void getKOrder(boolean pullToRefresh) {
        OrderRequest request = new OrderRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        int statusInt = 0;
        if (null != mRootView.getCache().get("status")) {
            statusInt = (int) mRootView.getCache().get("status");
        }
        String status = "";
//        1,2,3,5
        switch (statusInt) {
            case 0:
                status = "";
                break;
            case 1:
                status = "1";
                break;
            case 2:
                status = "31";
                break;
            case 3:
                status = "5";
                break;
        }
        request.setOrderStatus(status);
        request.setCmd(498);
        if (pullToRefresh) lastPageIndex = 1;
        request.setPageIndex(lastPageIndex);//下拉刷新默认只请求第一页

        mModel.getMyOrder(request)
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
                }).subscribe(new Consumer<OrderResponse>() {
            @Override
            public void accept(OrderResponse response) throws Exception {
                if (response.isSuccess()) {
                    // 数据转换
                    mRootView.showConent(response.getOrderList().size() > 0);
                    if (pullToRefresh) {
                        orderList.clear();
                    }
                    mRootView.setLoadedAllItems(response.getNextPageIndex() == -1);
                    orderList.addAll(response.getOrderList());
                    preEndIndex = orderList.size();//更新之前列表总长度,用于确定加载更多的起始位置
                    lastPageIndex = orderList.size() / 10;
                    if (pullToRefresh) {
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mAdapter.notifyItemRangeInserted(preEndIndex, orderList.size());
                    }
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
