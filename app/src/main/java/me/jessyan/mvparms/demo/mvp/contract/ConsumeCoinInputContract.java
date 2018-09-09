package me.jessyan.mvparms.demo.mvp.contract;

import android.app.Activity;

import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.GetRechargeListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.RechargeRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.GetRechargeListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.RechargeResponse;


public interface ConsumeCoinInputContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {

        Activity getActivity();

        void startLoadMore();

        void showError(boolean hasDate);

        void endLoadMore();

        void setLoadedAllItems(boolean has);

        Cache getCache();

    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<GetRechargeListResponse> getRechargeList(GetRechargeListRequest request);

        Observable<RechargeResponse> recharge(RechargeRequest request);
    }
}
