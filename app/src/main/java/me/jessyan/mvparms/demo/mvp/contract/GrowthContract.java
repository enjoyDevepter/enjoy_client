package me.jessyan.mvparms.demo.mvp.contract;

import android.app.Activity;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.request.AddressListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.GrowthInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.GrowthInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.GrowthListResponse;


public interface GrowthContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {

        Activity getActivity();

        void showConent(boolean hasData);

        void startLoadMore();

        void endLoadMore();

        void setLoadedAllItems(boolean has);

        void updateUI(GrowthInfoResponse response);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {

        Observable<GrowthInfoResponse> getGrowthInfo(GrowthInfoRequest request);

        Observable<GrowthListResponse> getGrowthList(AddressListRequest request);

    }
}
