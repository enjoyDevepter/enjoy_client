package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.model.entity.score.ScorePointBean;
import me.jessyan.mvparms.demo.mvp.model.entity.score.UserScorePageRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.score.UserScorePageResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import me.jessyan.mvparms.demo.mvp.contract.UserIntegralContract;

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


    public void requestOrderList(){
        requestOrderList(1,true);
    }

    public void nextPage(){
        requestOrderList(nextPageIndex,false);
    }

    private int nextPageIndex = 1;

    private void requestOrderList(int pageIndex,final boolean clear) {
        UserScorePageRequest userScorePageRequest = new UserScorePageRequest();
        userScorePageRequest.setPageIndex(pageIndex);
        userScorePageRequest.setPageSize(10);

        Cache<String,Object> cache= ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        String token=(String)cache.get(KEY_KEEP+"token");

        userScorePageRequest.setToken(token);

        mModel.requestScorePage(userScorePageRequest)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    if (clear) {
//                        mRootView.showLoading();//显示下拉刷新的进度条
                    }else
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
                            if(clear){
                                orderBeanList.clear();
                            }
                            nextPageIndex = response.getNextPageIndex();
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
}
