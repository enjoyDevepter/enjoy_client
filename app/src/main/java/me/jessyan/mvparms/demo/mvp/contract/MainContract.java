package me.jessyan.mvparms.demo.mvp.contract;

import android.app.Activity;

import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.Area;
import me.jessyan.mvparms.demo.mvp.model.entity.HomeAd;
import me.jessyan.mvparms.demo.mvp.model.entity.request.HomeADRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.CityResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HomeAdResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.LocationRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.UpdateRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.UserInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.UpdateResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.UserInfoResponse;


public interface MainContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        Activity getActivity();

        Cache getCache();

        void showSign(boolean show);

        void showAD(HomeAd ad);

        //申请权限
        RxPermissions getRxPermissions();

        void showUpdateInfo(UpdateResponse showUpdateInfo);

        void showLocationChange(Area county);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<UpdateResponse> checkUpdate(UpdateRequest request);

        Observable<CityResponse> getAreaForLoaction(LocationRequest request);

        Observable<HomeAdResponse> getOrCancelAD(HomeADRequest request);

        Observable<UserInfoResponse> getSignStatus(UserInfoRequest request);
    }
}
