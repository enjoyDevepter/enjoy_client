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
import me.jessyan.mvparms.demo.mvp.contract.InviteMainContract;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.MyMemberBean;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.FollowRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.GetMyMemberListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.GetMyMemberListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.ShareResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class InviteMainPresenter extends BasePresenter<InviteMainContract.Model, InviteMainContract.View> {
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
    List<MyMemberBean> orderBeanList;
    private int nextPageIndex = 1;

    @Inject
    public InviteMainPresenter(InviteMainContract.Model model, InviteMainContract.View rootView) {
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
    public void requestOrderList() {
        requestOrderList(1, true);
    }

    public void nextPage() {
        requestOrderList(nextPageIndex, false);
    }

    private void requestOrderList(int pageIndex, final boolean clear) {
        GetMyMemberListRequest request = new GetMyMemberListRequest();
        request.setPageIndex(pageIndex);
        request.setPageSize(10);
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(ArmsUtils.getContext()).extras();
        String token = (String) cache.get(KEY_KEEP + "token");
        request.setToken(token);


        mModel.getMyMemberList(request)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    if (clear) {
                        //                        mRootView.showLoading();//显示下拉刷新的进度条
                    } else
                        mRootView.startLoadMore();//显示上拉加载更多的进度条
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (clear)
                        mRootView.hideLoading();//隐藏下拉刷新的进度条
                    else
                        mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<GetMyMemberListResponse>(mErrorHandler) {
                    @Override
                    public void onNext(GetMyMemberListResponse response) {
                        if (response.isSuccess()) {
                            mRootView.updateUrl(response.getUrl());
                            if (clear) {
                                orderBeanList.clear();
                            }
                            nextPageIndex = response.getNextPageIndex();
                            mRootView.showError(response.getMemberList().size() > 0);
                            mRootView.setEnd(nextPageIndex == -1);
                            orderBeanList.addAll(response.getMemberList());
                            mAdapter.notifyDataSetChanged();
                            mRootView.hideLoading();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    public void invite() {
        FollowRequest request = new FollowRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(ArmsUtils.getContext()).extras();
        String token = (String) cache.get(KEY_KEEP + "token");
        request.setToken(token);
        request.setCmd(908);
        mModel.share(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<ShareResponse>(mErrorHandler) {
                    @Override
                    public void onNext(ShareResponse response) {
                        if (response.isSuccess()) {
                            mRootView.showWX(response.getShare());
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }
}
