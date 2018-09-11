package me.jessyan.mvparms.demo.mvp.contract;

import android.app.Activity;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.score.UserScorePageRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.score.UserScorePageResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.QiandaoInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.QiandaoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.UserInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.QiandaoInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.QiandaoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.UserInfoResponse;


public interface UserIntegralContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {

        void showConent(boolean hasData);

        void startLoadMore();

        void endLoadMore();

        void setLoadedAllItems(boolean has);

        Activity getActivity();

        void updateQiandaoInfo(boolean isSignin, long point, String url);

    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<UserScorePageResponse> requestScorePage(UserScorePageRequest request);

        Observable<QiandaoResponse> qiandao(QiandaoRequest request);

        Observable<UserInfoResponse> getUserInfo(UserInfoRequest request);

        Observable<QiandaoInfoResponse> getQiandaoInfo(QiandaoInfoRequest request);
    }
}
