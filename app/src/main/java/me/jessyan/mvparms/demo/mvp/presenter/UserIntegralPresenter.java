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

import org.simple.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.mvp.contract.UserIntegralContract;
import me.jessyan.mvparms.demo.mvp.model.MyModel;
import me.jessyan.mvparms.demo.mvp.model.entity.score.ScorePointBean;
import me.jessyan.mvparms.demo.mvp.model.entity.score.UserScorePageRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.score.UserScorePageResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.QiandaoInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.QiandaoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.UserInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.QiandaoInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.QiandaoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.UserInfoResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

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
    private int nextPageIndex = 1;

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

    public void requestOrderList() {
        requestOrderList(1, true);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void nextPage() {
        requestOrderList(nextPageIndex, false);
        getQiandaoInfo();
    }

    public void getQiandaoInfo(){
        QiandaoInfoRequest request = new QiandaoInfoRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        String token = (String) cache.get(KEY_KEEP + "token");
        request.setToken(token);

        mModel.getQiandaoInfo(request)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new Consumer<QiandaoInfoResponse>() {
                    @Override
                    public void accept(QiandaoInfoResponse response) throws Exception {
                        if (response.isSuccess()) {
                            mRootView.updateQiandaoInfo("1".equals(response.getIsSign()),response.getPoint(),response.getUrl());
                        } else {
                            mRootView.showMessage(response.getRetDesc());
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
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new Consumer<QiandaoResponse>() {
                    @Override
                    public void accept(QiandaoResponse response) throws Exception {
                        if (response.isSuccess()) {
                            ArmsUtils.makeText(ArmsUtils.getContext(), "签到成功");
                            getQiandaoInfo();
                            requestOrderList();
                        } else {
                            getQiandaoInfo();
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    private void requestOrderList(int pageIndex, final boolean clear) {
        UserScorePageRequest userScorePageRequest = new UserScorePageRequest();
        userScorePageRequest.setPageIndex(pageIndex);
        userScorePageRequest.setPageSize(10);

        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        String token = (String) cache.get(KEY_KEEP + "token");

        userScorePageRequest.setToken(token);

        mModel.requestScorePage(userScorePageRequest)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    if (clear) {
                        mRootView.showLoading();//显示下拉刷新的进度条
                    } else
                        mRootView.startLoadMore();//显示上拉加载更多的进度条
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (clear)
                        mRootView.hideLoading();//隐藏下拉刷新的进度条
                    else
                        mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new Consumer<UserScorePageResponse>() {
                    @Override
                    public void accept(UserScorePageResponse response) throws Exception {
                        if (response.isSuccess()) {
                            if (clear) {
                                orderBeanList.clear();
                            }
                            nextPageIndex = response.getNextPageIndex();
                            mRootView.showError(response.getPointList().size() > 0);
                            mRootView.setEnd(nextPageIndex == -1);
                            orderBeanList.addAll(response.getPointList());
                            mAdapter.notifyDataSetChanged();
                            mRootView.hideLoading();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    public void initUser() {
        UserInfoRequest request = new UserInfoRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(ArmsUtils.getContext()).extras();
        String token = (String) cache.get(KEY_KEEP + "token");
        request.setToken(token);

        mModel.getUserInfo(request)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();//显示下拉刷新的进度条
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();//隐藏下拉刷新的进度条
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new Consumer<UserInfoResponse>() {
                    @Override
                    public void accept(UserInfoResponse response) throws Exception {
                        if (response.isSuccess()) {
                            cache.put(KEY_KEEP + MyModel.KEY_FOR_USER_INFO, response.getMember());
                            cache.put(KEY_KEEP + MyModel.KEY_FOR_USER_ACCOUNT, response.getMemberAccount());
                            EventBus.getDefault().post(response.getMember(), EventBusTags.USER_INFO_CHANGE);
                            EventBus.getDefault().post(response.getMemberAccount(), EventBusTags.USER_ACCOUNT_CHANGE);
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

}
