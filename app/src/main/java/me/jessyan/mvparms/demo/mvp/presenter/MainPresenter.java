package me.jessyan.mvparms.demo.mvp.presenter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
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
import me.jessyan.mvparms.demo.mvp.contract.MainContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Area;
import me.jessyan.mvparms.demo.mvp.model.entity.request.HomeADRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.CityResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HomeAdResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.LocationRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.UpdateRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.UpdateResponse;
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
                        if (response.isSuccess()) {
                            if (!cancel && null != response.getAd()) {
                                mRootView.getCache().put("adId", response.getAd().getAdId());
                                mRootView.showAD(response.getAd());
                            }
                        } else {
                            mRootView.showMessage(response.getRetDesc());
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
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    public void getAreaForLoaction() {
        LocationRequest request = new LocationRequest();
        request.setLon((String) mRootView.getCache().get("lon"));
        request.setLat((String) mRootView.getCache().get("lat"));
        mModel.getAreaForLoaction(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<CityResponse>(mErrorHandler) {
                    @Override
                    public void onNext(CityResponse response) {
                        if (response.isSuccess()) {
                            Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
                            ArrayList<Area> areas = response.getAreaList();
                            for (Area provice : areas) {
                                if ("0".equals(provice.getParentId())) {
                                    cache.put("province", provice.getCode());
                                    for (Area city : areas) {
                                        if (city.getParentId().equals(provice.getId())) {
                                            cache.put("city", city.getCode());
                                            for (Area county : areas) {
                                                if (county.getParentId().equals(city.getId())) {
                                                    cache.put("county", county.getCode());
                                                    cache.put("current_location_info", provice.getName() + "-" + city.getName() + "-" + county.getName());
                                                    EventBus.getDefault().post(county, EventBusTags.CITY_CHANGE_EVENT);
                                                    break;
                                                }
                                            }
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    private void requestLocation() {
        LocationManager locationManager = (LocationManager) mRootView.getActivity().getSystemService(Context.LOCATION_SERVICE);
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000l, 0, (LocationListener) mRootView.getActivity());
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
