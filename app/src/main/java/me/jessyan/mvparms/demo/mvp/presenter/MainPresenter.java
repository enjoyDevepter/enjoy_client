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
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.PermissionUtil;

import java.util.List;

import javax.inject.Inject;

import me.jessyan.mvparms.demo.mvp.contract.MainContract;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


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
                    mRootView.showMessage("Request permissions failure");
                }

                @Override
                public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
                    mRootView.showMessage("Need to go to the settings");
                }
            }, mRootView.getRxPermissions(), mErrorHandler);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000l, 0, (LocationListener) mRootView.getActivity());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LocationManager locationManager = (LocationManager) mRootView.getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates((LocationListener) mRootView.getActivity());
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

}
