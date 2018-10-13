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
import me.jessyan.mvparms.demo.mvp.contract.SelfPickupAddrListContract;
import me.jessyan.mvparms.demo.mvp.model.entity.AreaAddress;
import me.jessyan.mvparms.demo.mvp.model.entity.CommonStoreDateType;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean.OrderBy;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.request.HospitalListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.response.HospitalListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.request.SimpleRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.StoresListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.AllAddressResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.StoresListResponse;
import me.jessyan.mvparms.demo.mvp.ui.activity.SelfPickupAddrListActivity;
import me.jessyan.mvparms.demo.mvp.ui.adapter.StoresListAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static me.jessyan.mvparms.demo.mvp.ui.activity.SelfPickupAddrListActivity.KEY_FOR_ACTIVITY_LIST_TYPE;


@ActivityScope
public class SelfPickupAddrListPresenter extends BasePresenter<SelfPickupAddrListContract.Model, SelfPickupAddrListContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    List<AreaAddress> addressList;
    @Inject
    StoresListAdapter mAdapter;
    @Inject
    List<CommonStoreDateType> commonStoreDateTypeList;

    private int preEndIndex;
    private int lastPageIndex = 1;

    @Inject
    public SelfPickupAddrListPresenter(SelfPickupAddrListContract.Model model, SelfPickupAddrListContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        getData(true);
        getAllAddressList();
    }


    private void getAllAddressList() {
        SimpleRequest request = new SimpleRequest();
        request.setCmd(902);

        mModel.getAllAddressList(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<AllAddressResponse>(mErrorHandler) {
                    @Override
                    public void onNext(AllAddressResponse response) {
                        if (response.isSuccess()) {
                            addressList.clear();
                            addressList.addAll(response.getAreaList());
                        }
                    }
                });
    }

    public void getData(boolean pullToRefresh) {
        SelfPickupAddrListActivity.ListType listType = (SelfPickupAddrListActivity.ListType) mRootView.getActivity().getIntent().getSerializableExtra(KEY_FOR_ACTIVITY_LIST_TYPE);
        switch (listType) {
            case HOP:
                getHospital(pullToRefresh);
                break;
            case STORE:
                getStores(pullToRefresh);
                break;
            case ADDR:
                break;
        }
    }

    private void getStores(boolean pullToRefresh) {
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();

        StoresListRequest request = new StoresListRequest();
        request.setCountyId(mRootView.getActivity().getIntent().getStringExtra("county"));
        request.setCityId(mRootView.getActivity().getIntent().getStringExtra("city"));
        request.setProvinceId(mRootView.getActivity().getIntent().getStringExtra("province"));
        request.setGoodsId(mRootView.getActivity().getIntent().getStringExtra("goodsId"));
        request.setMerchId(mRootView.getActivity().getIntent().getStringExtra("merchId"));
        request.setLon(String.valueOf(cache.get("lon")));
        request.setLat(String.valueOf(cache.get("lat")));
        if (pullToRefresh) lastPageIndex = 1;
        request.setPageIndex(lastPageIndex);//下拉刷新默认只请求第一页

        List<StoresListRequest.OrderBy> orderByList = new ArrayList<>();
        StoresListRequest.OrderBy orderBy = new StoresListRequest.OrderBy();
        orderBy.setField("distance");
        orderBy.setAsc(false);
        orderByList.add(orderBy);
        request.setOrderBys(orderByList);
        request.setLon(String.valueOf(cache.get("lon")));
        request.setLat(String.valueOf(cache.get("lat")));
        mModel.getStores(request)
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
                .subscribe(new ErrorHandleSubscriber<StoresListResponse>(mErrorHandler) {
                    @Override
                    public void onNext(StoresListResponse response) {
                        if (response.isSuccess()) {
                            mRootView.showConent(response.getStoreList().size() > 0);
                            if (pullToRefresh) {
                                commonStoreDateTypeList.clear();
                            }
                            mRootView.setLoadedAllItems(response.getNextPageIndex() == -1);
                            commonStoreDateTypeList.addAll(response.getStoreList());
                            preEndIndex = commonStoreDateTypeList.size();//更新之前列表总长度,用于确定加载更多的起始位置
                            lastPageIndex = commonStoreDateTypeList.size() / 10 + 1;
                            if (pullToRefresh) {
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mAdapter.notifyItemRangeInserted(preEndIndex, commonStoreDateTypeList.size());
                            }
                        }
                    }
                });
    }

    private void getHospital(boolean pullToRefresh) {
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        HospitalListRequest request = new HospitalListRequest();
        request.setCountyId(mRootView.getActivity().getIntent().getStringExtra("county"));
        request.setCityId(mRootView.getActivity().getIntent().getStringExtra("city"));
        request.setProvinceId(mRootView.getActivity().getIntent().getStringExtra("province"));
        request.setSpecValueId(mRootView.getActivity().getIntent().getStringExtra("specValueId"));
        request.setGoodsId(mRootView.getActivity().getIntent().getStringExtra("goodsId"));
        request.setMerchId(mRootView.getActivity().getIntent().getStringExtra("merchId"));
        request.setLon(String.valueOf(cache.get("lon")));
        request.setLat(String.valueOf(cache.get("lat")));
        if (pullToRefresh) lastPageIndex = 1;
        request.setPageIndex(lastPageIndex);//下拉刷新默认只请求第一页
        List<OrderBy> orderByList = new ArrayList<>();
        OrderBy orderBy = new OrderBy();
        orderBy.setField("distance");
        orderBy.setAsc(false);
        orderByList.add(orderBy);
        request.setOrderBys(orderByList);
        mModel.getHospitals(request)
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
                .subscribe(new ErrorHandleSubscriber<HospitalListResponse>(mErrorHandler) {
                    @Override
                    public void onNext(HospitalListResponse response) {
                        if (response.isSuccess()) {
                            mRootView.showConent(response.getHospitalList().size() > 0);
                            if (pullToRefresh) {
                                commonStoreDateTypeList.clear();
                            }
                            mRootView.setLoadedAllItems(response.getNextPageIndex() == -1);
                            commonStoreDateTypeList.addAll(response.getHospitalList());
                            preEndIndex = commonStoreDateTypeList.size();//更新之前列表总长度,用于确定加载更多的起始位置
                            lastPageIndex = commonStoreDateTypeList.size() / 10 + 1;
                            if (pullToRefresh) {
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mAdapter.notifyItemRangeInserted(preEndIndex, commonStoreDateTypeList.size());
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
