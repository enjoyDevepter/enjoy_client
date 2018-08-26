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
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.ConsumeCoinContract;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.BalanceBean;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.GetConsumeInfoPageRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.GetConsumeInfoPageResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class ConsumeCoinPresenter extends BasePresenter<ConsumeCoinContract.Model, ConsumeCoinContract.View> {
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
    List<BalanceBean> orderBeanList;
    private int nextPageIndex = 1;

    @Inject
    public ConsumeCoinPresenter(ConsumeCoinContract.Model model, ConsumeCoinContract.View rootView) {
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
    }

    private void requestOrderList(int pageIndex, final boolean clear) {
        GetConsumeInfoPageRequest request = new GetConsumeInfoPageRequest();
        request.setPageIndex(pageIndex);
        request.setPageSize(10);
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        String token = (String) cache.get(KEY_KEEP + "token");
        request.setToken(token);


        mModel.getconsumeInfoPage(request)
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
                .subscribe(new Consumer<GetConsumeInfoPageResponse>() {
                    @Override
                    public void accept(GetConsumeInfoPageResponse response) throws Exception {
                        if (response.isSuccess()) {
                            if (clear) {
                                orderBeanList.clear();
                            }
                            mRootView.showError(response.getBalanceList().size() > 0);
                            nextPageIndex = response.getNextPageIndex();
                            mRootView.setEnd(nextPageIndex == -1);
                            orderBeanList.addAll(response.getBalanceList());
                            mAdapter.notifyDataSetChanged();
                            mRootView.hideLoading();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

}
