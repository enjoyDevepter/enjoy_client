package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v7.widget.RecyclerView;

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
import me.jessyan.mvparms.demo.mvp.contract.GrowthContract;
import me.jessyan.mvparms.demo.mvp.model.entity.request.AddressListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.GrowthInfo;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.GrowthInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.GrowthInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.GrowthListResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class GrowthPresenter extends BasePresenter<GrowthContract.Model, GrowthContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;

    @Inject
    List<GrowthInfo> growthInfoList;
    @Inject
    RecyclerView.Adapter mAdapter;

    private int preEndIndex;
    private int lastPageIndex = 1;

    @Inject
    public GrowthPresenter(GrowthContract.Model model, GrowthContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        getGrowthInfo();
        getGrowthList(true);
    }

    private void getGrowthInfo() {
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        GrowthInfoRequest request = new GrowthInfoRequest();
        request.setToken(String.valueOf(cache.get(KEY_KEEP + "token")));

        mModel.getGrowthInfo(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<GrowthInfoResponse>(mErrorHandler) {
                    @Override
                    public void onNext(GrowthInfoResponse response) {
                        if (response.isSuccess()) {
                            mRootView.updateUI(response);
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }


    public void getGrowthList(boolean pullToRefresh) {
        AddressListRequest request = new AddressListRequest();
        request.setCmd(202);
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        request.setToken(String.valueOf(cache.get(KEY_KEEP + "token")));
        if (pullToRefresh) lastPageIndex = 1;
        request.setPageIndex(lastPageIndex);//下拉刷新默认只请求第一页

        mModel.getGrowthList(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    mRootView.startLoadMore();//显示上拉加载更多的进度条
                })
                .doFinally(() -> {
                    mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                })
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<GrowthListResponse>(mErrorHandler) {
                    @Override
                    public void onNext(GrowthListResponse response) {
                        if (response.isSuccess()) {
                            if (null == response.getGrowthList()) {
                                mRootView.showConent(false);
                                return;
                            }
                            mRootView.showConent(response.getGrowthList().size() > 0);
                            mRootView.setLoadedAllItems(response.getNextPageIndex() == -1);
                            growthInfoList.addAll(response.getGrowthList());
                            preEndIndex = growthInfoList.size();//更新之前列表总长度,用于确定加载更多的起始位置
                            lastPageIndex = growthInfoList.size() / 10;
                            if (pullToRefresh) {
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mAdapter.notifyItemRangeInserted(preEndIndex, growthInfoList.size());
                            }
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
