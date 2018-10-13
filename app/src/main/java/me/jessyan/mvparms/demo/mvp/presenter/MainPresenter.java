package me.jessyan.mvparms.demo.mvp.presenter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.PermissionUtil;
import com.jess.arms.utils.RxLifecycleUtils;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.app.utils.SPUtils;
import me.jessyan.mvparms.demo.mvp.contract.MainContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Area;
import me.jessyan.mvparms.demo.mvp.model.entity.request.HomeADRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.CityResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HomeAdResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.LocationRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.UpdateRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.UserInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.UpdateResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.UserInfoResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class MainPresenter extends BasePresenter<MainContract.Model, MainContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;

    @Inject
    public MainPresenter(MainContract.Model model, MainContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        requestLocation();//打开 App 时自动加载列表
        checkUpdate();
        getOrCancelAd(false);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResume() {
        getSignStatus();
    }

    private void getSignStatus() {
        UserInfoRequest request = new UserInfoRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(ArmsUtils.getContext()).extras();
        request.setCmd(104);
        String token = (String) cache.get(KEY_KEEP + "token");
        if (ArmsUtils.isEmpty(token)) {
            return;
        }
        request.setToken(token);
        mModel.getSignStatus(request)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();//显示下拉刷新的进度条
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();//隐藏下拉刷新的进度条
                })
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<UserInfoResponse>(mErrorHandler) {
                    @Override
                    public void onNext(UserInfoResponse response) {
                        if (response.isNeedLogin()) {
                            cache.remove(KEY_KEEP + "token");
                            return;
                        }
                        if (response.isSuccess()) {
                            mRootView.showSign("0".equals(response.getMember().getIsSignin()) ? true : false);
                        }
                    }
                });
    }

    public void getOrCancelAd(boolean cancel) {
        HomeADRequest request = new HomeADRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        String token = (String) (cache.get(KEY_KEEP + "token"));
        if (!cancel) {
            if (ArmsUtils.isEmpty(token)) {
                request.setCmd(914);
            } else {
                request.setCmd(915);
            }
        } else {
            if (ArmsUtils.isEmpty(token)) {
                request.setCmd(916);
            } else {
                request.setCmd(917);
            }
        }
        request.setAdId((String) mRootView.getCache().get("adId"));
        request.setToken(token);
        mModel.getOrCancelAD(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<HomeAdResponse>(mErrorHandler) {
                    @Override
                    public void onNext(HomeAdResponse response) {
                        if (response.isNeedLogin()) {
                            cache.remove(KEY_KEEP + "token");
                            getOrCancelAd(cancel);
                            return;
                        }
                        if (response.isSuccess()) {
                            if (!cancel && null != response.getAd()) {
                                mRootView.getCache().put("adId", response.getAd().getAdId());
                                mRootView.showAD(response.getAd());
                            }
                        }
                    }
                });
    }

    private void checkUpdate() {
        if (ActivityCompat.checkSelfPermission(mRootView.getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            //请求外部存储权限用于适配android6.0的权限管理机制
            PermissionUtil.readPhoneState(new PermissionUtil.RequestPermission() {
                @Override
                public void onRequestPermissionSuccess() {
                    //request permission success, do something.
                    checkUpdateForApp();
                }

                @Override
                public void onRequestPermissionFailure(List<String> permissions) {
                }

                @Override
                public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
                }
            }, mRootView.getRxPermissions(), mErrorHandler);
        } else {
            checkUpdateForApp();
        }
    }

    private void checkUpdateForApp() {
        UpdateRequest request = new UpdateRequest();
        request.setType("android");
        mModel.checkUpdate(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<UpdateResponse>(mErrorHandler) {
                    @Override
                    public void onNext(UpdateResponse response) {
                        if (response.isSuccess()) {
                            mRootView.showUpdateInfo(response);
                        }
                    }
                });
    }

    public void getAreaForLoaction() {
        Cache<String, Object> globalCache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        LocationRequest request = new LocationRequest();
        request.setLon((String) mRootView.getCache().get("lon"));
        request.setLat((String) mRootView.getCache().get("lat"));
        globalCache.put("lon", mRootView.getCache().get("lon"));
        globalCache.put("lat", mRootView.getCache().get("lat"));
        mModel.getAreaForLoaction(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<CityResponse>(mErrorHandler) {
                    @Override
                    public void onNext(CityResponse response) {
                        if (response.isSuccess()) {
                            Cache cache = mRootView.getCache();
                            ArrayList<Area> areas = response.getAreaList();
                            String provinceId, cityId, countyId;
                            for (Area provice : areas) {
                                if ("0".equals(provice.getParentId())) {
                                    provinceId = provice.getId();
                                    cache.put("province", provinceId);
                                    for (Area city : areas) {
                                        if (city.getParentId().equals(provice.getId())) {
                                            cityId = provice.getId();
                                            cache.put("city", cityId);
                                            for (Area county : areas) {
                                                if (county.getParentId().equals(city.getId())) {
                                                    countyId = county.getId();
                                                    cache.put("county", countyId);
                                                    globalCache.put("current_location_info", provice.getName() + "-" + city.getName() + "-" + county.getName());
                                                    if (!ArmsUtils.isEmpty((String) globalCache.get("province"))) {
                                                        if (!provinceId.equals(globalCache.get("province"))
                                                                || !cityId.equals(globalCache.get("city"))
                                                                || !countyId.equals(globalCache.get("county"))) {
                                                            mRootView.showLocationChange(county);
                                                        }
                                                    } else {
                                                        globalCache.put("province", provinceId);
                                                        SPUtils.put("province", provinceId);
                                                        globalCache.put("city", cityId);
                                                        SPUtils.put("city", cityId);
                                                        globalCache.put("county", countyId);
                                                        SPUtils.put("county", countyId);
                                                        SPUtils.put("countyName", county.getName());
                                                        EventBus.getDefault().post(county, EventBusTags.CITY_CHANGE_EVENT);
                                                    }
                                                    break;
                                                }
                                            }
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                });
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(mRootView.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mRootView.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            PermissionUtil.locaiton(new PermissionUtil.RequestPermission() {
                @SuppressLint("MissingPermission")
                @Override
                public void onRequestPermissionSuccess() {
                    //request permission success, do something.
                }

                @Override
                public void onRequestPermissionFailure(List<String> permissions) {
                }

                @Override
                public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
                }
            }, mRootView.getRxPermissions(), mErrorHandler);
            return;
        }
    }


    @Override
    public void onDestroy() {
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
        super.onDestroy();
    }
}
