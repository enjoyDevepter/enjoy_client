package me.jessyan.mvparms.demo.mvp.contract;

import android.app.Activity;

import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.request.PickCouponRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.PickCouponListResponse;


public interface PickCouponContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        Activity getActivity();

        Cache getCache();

        void updateUI(String bgURL);

    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<PickCouponListResponse> getPickCouponList(PickCouponRequest request);

        Observable<BaseResponse> pickCoupon(PickCouponRequest request);
    }
}
