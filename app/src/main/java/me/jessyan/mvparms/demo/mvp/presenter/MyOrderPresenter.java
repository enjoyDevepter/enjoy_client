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

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.MyOrderContract;
import me.jessyan.mvparms.demo.mvp.model.entity.order.Order;
import me.jessyan.mvparms.demo.mvp.model.entity.request.OrderRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.OrderResponse;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyOrderAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

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
                getSOrder(pullToRefresh);
                break;
            case 1:
                getKOrder(pullToRefresh);
                break;
            case 2:
                getHOrder(pullToRefresh);
                break;
        }
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
                } else {
                    mRootView.showMessage(response.getRetDesc());
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
                status = "3";
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
                } else {
                    mRootView.showMessage(response.getRetDesc());
                }
            }
        });
    }

    /**
     * 生美订单
     */
    private void getKOrder(boolean pullToRefresh) {
        int statusInt = 0;
        if (null != mRootView.getCache().get("status")) {
            statusInt = (int) mRootView.getCache().get("status");
        }
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
