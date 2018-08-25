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
import me.jessyan.mvparms.demo.mvp.contract.TaoCanContract;
import me.jessyan.mvparms.demo.mvp.model.entity.MealGoods;
import me.jessyan.mvparms.demo.mvp.model.entity.request.MealListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.MealListResponse;
import me.jessyan.mvparms.demo.mvp.ui.adapter.TaoCanListAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class TaoCanPresenter extends BasePresenter<TaoCanContract.Model, TaoCanContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    List<MealGoods> mGoods;
    @Inject
    TaoCanListAdapter mAdapter;

    private int preEndIndex;
    private int lastPageIndex = 1;

    @Inject
    public TaoCanPresenter(TaoCanContract.Model model, TaoCanContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        getTaoCan(true); // 进入界面后加载
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public void getTaoCan(boolean pullToRefresh) {

        MealListRequest request = new MealListRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        request.setCity((String) (cache.get("city")));
        request.setCounty((String) (cache.get("county")));
        request.setProvince((String) (cache.get("province")));

        if (pullToRefresh) lastPageIndex = 1;
        request.setPageIndex(lastPageIndex);//下拉刷新默认只请求第一页

        mModel.getTaoCanList(request)
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
                }).subscribe(new Consumer<MealListResponse>() {
            @Override
            public void accept(MealListResponse response) throws Exception {
                if (response.isSuccess()) {
                    if (pullToRefresh) {
                        mGoods.clear();
                    }
                    mRootView.setLoadedAllItems(response.getNextPageIndex() == -1);
                    mGoods.addAll(response.getSetMealGoodsList());
                    preEndIndex = mGoods.size();//更新之前列表总长度,用于确定加载更多的起始位置
                    lastPageIndex = mGoods.size() / 10;
                    if (pullToRefresh) {
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mAdapter.notifyItemRangeInserted(preEndIndex, mGoods.size());
                    }
                } else {
                    mRootView.showMessage(response.getRetDesc());
                }
            }
        });
    }
}
