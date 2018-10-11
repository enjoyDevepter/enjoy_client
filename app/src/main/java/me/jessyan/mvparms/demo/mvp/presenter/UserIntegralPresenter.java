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
import me.jessyan.mvparms.demo.mvp.contract.UserIntegralContract;
import me.jessyan.mvparms.demo.mvp.model.entity.score.ScorePointBean;
import me.jessyan.mvparms.demo.mvp.model.entity.score.UserScorePageRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.score.UserScorePageResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.QiandaoInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.QiandaoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.QiandaoInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.QiandaoResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class UserIntegralPresenter extends BasePresenter<UserIntegralContract.Model, UserIntegralContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    RecyclerView.Adapter mAdapter;
    @Inject
    List<ScorePointBean> orderBeanList;
    private int preEndIndex;
    private int lastPageIndex = 1;

    @Inject
    public UserIntegralPresenter(UserIntegralContract.Model model, UserIntegralContract.View rootView) {
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

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onCreate() {
        requestOrderList(true);
        getQiandaoInfo();
    }


    public void getQiandaoInfo() {
        QiandaoInfoRequest request = new QiandaoInfoRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        String token = (String) cache.get(KEY_KEEP + "token");
        request.setToken(token);

        mModel.getQiandaoInfo(request)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                })
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<QiandaoInfoResponse>(mErrorHandler) {
                    @Override
                    public void onNext(QiandaoInfoResponse response) {
                        if (response.isSuccess()) {
                            mRootView.updateQiandaoInfo("1".equals(response.getIsSign()), response.getPoint(), response.getUrl());
                        }
                    }
                });
    }

    public void qiandao() {

        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        String token = (String) cache.get(KEY_KEEP + "token");

        QiandaoRequest request = new QiandaoRequest();
        request.setToken(token);

        mModel.qiandao(request)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                })
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<QiandaoResponse>(mErrorHandler) {
                    @Override
                    public void onNext(QiandaoResponse response) {
                        if (response.isSuccess()) {
                            ArmsUtils.makeText(ArmsUtils.getContext(), "签到成功");
                            getQiandaoInfo();
                            requestOrderList(true);
                        } else {
                            getQiandaoInfo();
                        }
                    }
                });
    }

    public void requestOrderList(boolean pullToRefresh) {
        UserScorePageRequest userScorePageRequest = new UserScorePageRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        String token = (String) cache.get(KEY_KEEP + "token");
        userScorePageRequest.setToken(token);

        if (pullToRefresh) lastPageIndex = 1;
        userScorePageRequest.setPageIndex(lastPageIndex);//下拉刷新默认只请求第一页

        mModel.requestScorePage(userScorePageRequest)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    if (pullToRefresh) {
                        mRootView.showLoading();//显示下拉刷新的进度条
                    } else
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
                .subscribe(new ErrorHandleSubscriber<UserScorePageResponse>(mErrorHandler) {
                    @Override
                    public void onNext(UserScorePageResponse response) {
                        if (response.isSuccess()) {
                            if (pullToRefresh) {
                                orderBeanList.clear();
                            }
                            mRootView.setLoadedAllItems(response.getNextPageIndex() == -1);
                            orderBeanList.addAll(response.getPointList());
                            preEndIndex = orderBeanList.size();//更新之前列表总长度,用于确定加载更多的起始位置
                            lastPageIndex = orderBeanList.size() / 10;
                            if (pullToRefresh) {
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mAdapter.notifyItemRangeInserted(preEndIndex, orderBeanList.size());
                            }
                        }
                    }
                });
    }
}
