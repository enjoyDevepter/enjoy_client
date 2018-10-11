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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.HospitalContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Hospital;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean.OrderBy;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.request.HospitalListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.response.HospitalResponse;
import me.jessyan.mvparms.demo.mvp.ui.adapter.HospitalListAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;


@ActivityScope
public class HospitalPresenter extends BasePresenter<HospitalContract.Model, HospitalContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    List<Hospital> hospitalList;
    @Inject
    HospitalListAdapter mAdapter;
    private int preEndIndex;
    private int lastPageIndex = 1;

    @Inject
    public HospitalPresenter(HospitalContract.Model model, HospitalContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        getHospitalList(true);
    }


    public void getHospitalList(boolean pullToRefresh) {
        HospitalListRequest request = new HospitalListRequest();
        request.setCmd(606);
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setCityId(String.valueOf(cache.get("city")));
        request.setCountyId(String.valueOf(cache.get("county")));
        request.setProvinceId(String.valueOf(cache.get("province")));

        if (pullToRefresh) lastPageIndex = 1;
        request.setPageIndex(lastPageIndex);//下拉刷新默认只请求第一页
        List<OrderBy> orderByList = new ArrayList<>();
        if (null != mRootView.getCache().get("distance")) {
            OrderBy orderBy = new OrderBy();
            orderBy.setField("distance");
            orderBy.setAsc((Boolean) mRootView.getCache().get("distance"));
            orderByList.add(orderBy);
            request.setOrderBys(orderByList);
        }
        mModel.getHospitalList(request)
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
                .subscribe(new ErrorHandleSubscriber<HospitalResponse>(mErrorHandler) {
                    @Override
                    public void onNext(HospitalResponse response) {
                        if (response.isSuccess()) {
                            mRootView.showContent(response.getHospitalList().size() > 0);
                            if (pullToRefresh) {
                                hospitalList.clear();
                            }
                            mRootView.setLoadedAllItems(response.getNextPageIndex() == -1);
                            hospitalList.addAll(response.getHospitalList());
                            preEndIndex = hospitalList.size();//更新之前列表总长度,用于确定加载更多的起始位置
                            lastPageIndex = hospitalList.size() / 10;
                            if (pullToRefresh) {
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mAdapter.notifyItemRangeInserted(preEndIndex, hospitalList.size());
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
