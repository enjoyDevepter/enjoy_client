package me.jessyan.mvparms.demo.mvp.contract;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.GetCashRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.UserInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.GetCashResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.UserInfoResponse;


public interface GetCashContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void showOk();
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<GetCashResponse> getCash(GetCashRequest request);
        Observable<UserInfoResponse> getUserInfo(UserInfoRequest request);
    }
}
