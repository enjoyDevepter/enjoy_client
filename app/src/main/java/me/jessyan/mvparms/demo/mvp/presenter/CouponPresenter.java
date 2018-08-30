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
import me.jessyan.mvparms.demo.mvp.contract.CouponContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Coupon;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.MyCouponListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.MyCouponListResponse;
import me.jessyan.mvparms.demo.mvp.ui.adapter.CouponListAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class CouponPresenter extends BasePresenter<CouponContract.Model, CouponContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    List<Coupon> couponList;
    @Inject
    CouponListAdapter mAdapter;
    private int preEndIndex;
    private int lastPageIndex = 1;

    @Inject
    public CouponPresenter(CouponContract.Model model, CouponContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        String type = mRootView.getActivity().getIntent().getStringExtra("type");
        if (ArmsUtils.isEmpty(type)) {
            getMyCouponList(true);
        } else {
            couponList.clear();
            couponList.addAll(mRootView.getActivity().getIntent().getParcelableArrayListExtra("coupons"));
            mAdapter.notifyDataSetChanged();
        }
    }

    public void getMyCouponList(boolean pullToRefresh) {
        MyCouponListRequest request = new MyCouponListRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        request.setStatus(String.valueOf(mRootView.getCache().get("status")));
        if (pullToRefresh) lastPageIndex = 1;
        request.setPageIndex(lastPageIndex);//下拉刷新默认只请求第一页

        mModel.getMyCouponList(request)
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
                .subscribe(new ErrorHandleSubscriber<MyCouponListResponse>(mErrorHandler) {
                    @Override
                    public void onNext(MyCouponListResponse response) {
                        if (response.isSuccess()) {
                            if (pullToRefresh) {
                                couponList.clear();
                            }
                            mRootView.showError(response.getCouponList().size() > 0);
                            mRootView.setLoadedAllItems(response.getNextPageIndex() == -1);
                            couponList.addAll(response.getCouponList());
                            preEndIndex = couponList.size();//更新之前列表总长度,用于确定加载更多的起始位置
                            lastPageIndex = couponList.size() / 10;
                            if (pullToRefresh) {
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mAdapter.notifyItemRangeInserted(preEndIndex, couponList.size());
                            }
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }
}
