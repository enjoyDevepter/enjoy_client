package me.jessyan.mvparms.demo.mvp.contract;

import android.app.Activity;

import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.ModifyAppointmentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.MyMealDetailRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.ShareRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.AppointmentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryApplyResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.Share;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.ShareResponse;


public interface MyMealDetailsContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void startLoadMore();

        void endLoadMore();

        void setLoadedAllItems(boolean has);

        Activity getActivity();

        Cache getCache();

        void showError(boolean hasDate);

        void share(Share share);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {

        Observable<AppointmentResponse> getAppointment(MyMealDetailRequest request);

        Observable<BaseResponse> cancelAppointment(ModifyAppointmentRequest request);

        Observable<ShareResponse> share(ShareRequest request);

        Observable<DiaryApplyResponse> apply(DiaryRequest request);
    }
}
