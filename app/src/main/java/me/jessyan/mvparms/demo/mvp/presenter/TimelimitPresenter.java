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
import me.jessyan.mvparms.demo.mvp.contract.TimelimitContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.model.entity.request.GoodsListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.GoodsListResponse;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsLimitListAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;


@ActivityScope
public class TimelimitPresenter extends BasePresenter<TimelimitContract.Model, TimelimitContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    List<Goods> mGoods;
    @Inject
    GoodsLimitListAdapter mAdapter;
    @Inject
    ImageLoader mImageLoader;
    private int preEndIndex;
    private int lastPageIndex = 1;

    @Inject
    public TimelimitPresenter(TimelimitContract.Model model, TimelimitContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onCreate() {
        getTimeLimitGoodsList(true);
    }

    public void getTimeLimitGoodsList(boolean pullToRefresh) {
        GoodsListRequest request = new GoodsListRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setCmd(460);
        request.setProvince(String.valueOf(cache.get("province")));
        request.setCity(String.valueOf(cache.get("city")));
        request.setCounty(String.valueOf(cache.get("county")));
        request.setCategoryId((String) mRootView.getCache().get("categoryId"));
        request.setSecondCategoryId((String) (mRootView.getCache().get("secondCategoryId")));
        request.setFirstCategoryId((String) (mRootView.getCache().get("firstCategoryId")));
        if (!ArmsUtils.isEmpty(String.valueOf(mRootView.getCache().get("orderByField")))) {
            GoodsListRequest.OrderBy orderBy = new GoodsListRequest.OrderBy();
            orderBy.setField((String) mRootView.getCache().get("orderByField"));
            orderBy.setAsc((Boolean) mRootView.getCache().get("orderByAsc"));
            request.setOrderBy(orderBy);
        }

        if (pullToRefresh) lastPageIndex = 1;
        request.setPageIndex(lastPageIndex);//下拉刷新默认只请求第一页

        mModel.getTimeLimitGoodsList(request)
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
                .subscribe(new ErrorHandleSubscriber<GoodsListResponse>(mErrorHandler) {
                    @Override
                    public void onNext(GoodsListResponse response) {
                        if (response.isSuccess()) {
                            if (pullToRefresh) {
                                mGoods.clear();
                            }
                            mRootView.setLoadedAllItems(response.getNextPageIndex() == -1);
                            mGoods.addAll(response.getGoodsList());
                            preEndIndex = mGoods.size();//更新之前列表总长度,用于确定加载更多的起始位置
                            lastPageIndex = mGoods.size() / 10 + 1;
                            if (pullToRefresh) {
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mAdapter.notifyItemRangeInserted(preEndIndex, mGoods.size());
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
