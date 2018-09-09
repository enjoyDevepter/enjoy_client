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

import cn.ehanmy.pay.PayManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.ConsumeCoinInputContract;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.ChargeBean;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.GetRechargeListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.RechargeRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.GetRechargeListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.RechargeResponse;
import me.jessyan.mvparms.demo.mvp.ui.adapter.ConsumeInputAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class ConsumeCoinInputPresenter extends BasePresenter<ConsumeCoinInputContract.Model, ConsumeCoinInputContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    ConsumeInputAdapter mAdapter;
    @Inject
    List<ChargeBean> orderBeanList;
    private int preEndIndex;
    private int lastPageIndex = 1;

    @Inject
    public ConsumeCoinInputPresenter(ConsumeCoinInputContract.Model model, ConsumeCoinInputContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void requestOrderList(){
        requestOrderList(false);
    }

    public void requestOrderList(boolean pullToRefresh) {
        GetRechargeListRequest request = new GetRechargeListRequest();
        request.setPageSize(10);

        Cache<String,Object> cache= ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        String token=(String)cache.get(KEY_KEEP+"token");
        request.setToken(token);
        if (pullToRefresh) lastPageIndex = 1;
        request.setPageIndex(lastPageIndex);//下拉刷新默认只请求第一页
        mModel.getRechargeList(request)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    if (pullToRefresh) {
                        //                        mRootView.showLoading();//显示下拉刷新的进度条
                    }else
                        mRootView.startLoadMore();//显示上拉加载更多的进度条
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (pullToRefresh)
                        mRootView.hideLoading();//隐藏下拉刷新的进度条
                    else
                        mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<GetRechargeListResponse>(mErrorHandler) {
                    @Override
                    public void onNext(GetRechargeListResponse response) {
                        if (response.isSuccess()) {

                            if (pullToRefresh) {
                                orderBeanList.clear();
                            }
                            orderBeanList.addAll(response.getChargeList());
                            mRootView.showError(response.getChargeList().size() > 0);
                            mRootView.setLoadedAllItems(response.getNextPageIndex() == -1);
                            preEndIndex = orderBeanList.size();//更新之前列表总长度,用于确定加载更多的起始位置
                            lastPageIndex = orderBeanList.size() / 10;
                            if (pullToRefresh) {
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mAdapter.notifyItemRangeInserted(preEndIndex, orderBeanList.size());
                            }
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    public void recharge(long money,String payType) {// 单位分
        RechargeRequest request = new RechargeRequest();
        request.setAmount(money);
        request.setType("1");
        request.setPayId(payType);
        Cache<String,Object> cache= ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        String token=(String)cache.get(KEY_KEEP+"token");
        request.setToken(token);

        mModel.recharge(request)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();//显示下拉刷新的进度条
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();//隐藏下拉刷新的进度条
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<RechargeResponse>(mErrorHandler) {
                    @Override
                    public void onNext(RechargeResponse response) {
                        if (response.isSuccess()) {
                            PayManager.getInstace(mRootView.getActivity()).toPay("ALIPAY_APP".equals(payType) ? PayManager.PayMode.ALIPAY : PayManager.PayMode.WXPAY, response.getParams(), (PayManager.PayListener) mRootView.getActivity());
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
