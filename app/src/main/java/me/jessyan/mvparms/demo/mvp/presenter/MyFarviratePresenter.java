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
import me.jessyan.mvparms.demo.mvp.contract.MyFarvirateContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.model.entity.response.GoodsListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.MyCouponListRequest;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyFarvirateGoodsListAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class MyFarviratePresenter extends BasePresenter<MyFarvirateContract.Model, MyFarvirateContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    List<Goods> goodsList;
    @Inject
    MyFarvirateGoodsListAdapter mAdapter;

    private int preEndIndex;
    private int lastPageIndex = 1;

    @Inject
    public MyFarviratePresenter(MyFarvirateContract.Model model, MyFarvirateContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onCreate() {
        getMyFarvirate(true);
    }

    public void getMyFarvirate(boolean pullToRefresh) {

        MyCouponListRequest request = new MyCouponListRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        String type = (String) mRootView.getCache().get("status");
        request.setType(type);
        if ("4".equals(type)) {
            request.setCmd(1161);
        } else {
            request.setCmd(1160);
        }
        if (pullToRefresh) lastPageIndex = 1;
        request.setPageIndex(lastPageIndex);//下拉刷新默认只请求第一页

        mModel.getMyFarvirate(request)
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
                            if (response.getCmd() == 1160) {
                                mRootView.showConent(response.getGoodsList().size() > 0);
                            } else {
                                mRootView.showConent(response.getSetMealGoodsList().size() > 0);
                            }
                            if (pullToRefresh) {
                                goodsList.clear();
                            }
                            mRootView.setLoadedAllItems(response.getNextPageIndex() == -1);
                            goodsList.addAll(response.getCmd() == 1160 ? response.getGoodsList() : response.getSetMealGoodsList());
                            preEndIndex = goodsList.size();//更新之前列表总长度,用于确定加载更多的起始位置
                            lastPageIndex = goodsList.size() / 10;
                            if (pullToRefresh) {
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mAdapter.notifyItemRangeInserted(preEndIndex, goodsList.size());
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
