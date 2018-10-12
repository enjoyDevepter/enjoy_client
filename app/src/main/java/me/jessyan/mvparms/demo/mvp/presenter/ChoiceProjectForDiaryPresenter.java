package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import com.jess.arms.base.DefaultAdapter;
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
import me.jessyan.mvparms.demo.mvp.contract.ChoiceProjectForDiaryContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.model.entity.diary.ProjectRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DirayProjectListResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class ChoiceProjectForDiaryPresenter extends BasePresenter<ChoiceProjectForDiaryContract.Model, ChoiceProjectForDiaryContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    List<Goods> mGoods;
    @Inject
    DefaultAdapter mAdapter;
    private int preEndIndex;
    private int lastPageIndex = 1;

    @Inject
    public ChoiceProjectForDiaryPresenter(ChoiceProjectForDiaryContract.Model model, ChoiceProjectForDiaryContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void initAppointment() {
        getProjects(true);
    }

    public void getProjects(boolean pullToRefresh) {
        ProjectRequest request = new ProjectRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        String token = (String) cache.get(KEY_KEEP + "token");
        request.setToken(token);
        request.setOrderId(mRootView.getActivity().getIntent().getStringExtra("orderId"));
        if (pullToRefresh) lastPageIndex = 1;
        request.setPageIndex(lastPageIndex);//下拉刷新默认只请求第一页

        mModel.getProjects(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
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
                .subscribe(new ErrorHandleSubscriber<DirayProjectListResponse>(mErrorHandler) {
                    @Override
                    public void onNext(DirayProjectListResponse response) {
                        if (response.isSuccess()) {
                            if (pullToRefresh) {
                                mGoods.clear();
                            }
                            mRootView.showConent(response.getGoodsList().size() > 0);
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
