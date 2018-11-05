package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.text.TextUtils;

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
import me.jessyan.mvparms.demo.mvp.contract.StoreInfoContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean.HospitalEnvBean;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.request.ActivityInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.response.ActivityInfoListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.request.GoodsListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.StoreInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.GoodsListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.StoreInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.FollowRequest;
import me.jessyan.mvparms.demo.mvp.ui.activity.LoginActivity;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.HospitalEnvImageAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class StoreInfoPresenter extends BasePresenter<StoreInfoContract.Model, StoreInfoContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    List<Goods> mGoods;
    @Inject
    GoodsListAdapter mAdapter;
    @Inject
    HospitalEnvImageAdapter storeEnvImageAdapter;
    @Inject
    List<String> imageList;

    private int preEndIndex;
    private int lastPageIndex = 1;

    @Inject
    public StoreInfoPresenter(StoreInfoContract.Model model, StoreInfoContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void init() {
        getStoreInfo();
        getActivityList();
        getGoodsList(true);
    }

    private void getStoreInfo() {
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        String token = (String) cache.get(KEY_KEEP + "token");
        StoreInfoRequest request = new StoreInfoRequest();
        if (TextUtils.isEmpty(token)) {
            request.setCmd(705);
        } else {
            request.setCmd(706);
        }
        request.setToken(token);
        request.setStoreId((String) mRootView.getCache().get("store_id"));
        mModel.getStoreInfo(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<StoreInfoResponse>(mErrorHandler) {
                    @Override
                    public void onNext(StoreInfoResponse response) {
                        if (response.isSuccess()) {
                            mRootView.updateStoreInfo(response.getStore());
                            imageList.clear();
                            for (HospitalEnvBean env : response.getStoreEnvList()) {
                                imageList.add(env.getImage());
                            }
                            storeEnvImageAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void getActivityList() {
        ActivityInfoRequest request = new ActivityInfoRequest();
        request.setCmd(938);
        mModel.getActivityList(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<ActivityInfoListResponse>(mErrorHandler) {
                    @Override
                    public void onNext(ActivityInfoListResponse response) {
                        if (response.isSuccess()) {
                            mRootView.updateActivityInfo(response.getActivityInfoList());
                        }
                    }
                });
    }

    public void getGoodsList(final boolean pullToRefresh) {
        GoodsListRequest request = new GoodsListRequest();
        request.setCmd(500);
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setHospitalId((String) mRootView.getCache().get("store_id"));
        request.setCity(String.valueOf(cache.get("city")));
        request.setCounty(String.valueOf(cache.get("county")));
        request.setProvince(String.valueOf(cache.get("province")));

        if (pullToRefresh) lastPageIndex = 1;
        request.setPageIndex(lastPageIndex);//下拉刷新默认只请求第一页

        mModel.getGoodsList(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    if (pullToRefresh) {
                        mRootView.showLoading();//显示下拉刷新的进度条
                    } else {
                        mRootView.startLoadMore();//显示上拉加载更多的进度条
                    }
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

    public void follow(boolean follow) {
        if (checkLoginStatus()) {
            ArmsUtils.startActivity(LoginActivity.class);
            return;
        }
        FollowRequest request = new FollowRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        request.setCmd(follow ? 707 : 708);
        request.setStoreId((String) mRootView.getCache().get("store_id"));
        mModel.follow(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.isSuccess()) {
                            mRootView.updatefollowStatus(follow);
                        }
                    }
                });
    }


    private boolean checkLoginStatus() {
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        String token = (String) (cache.get(KEY_KEEP + "token"));
        return ArmsUtils.isEmpty(token);
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
