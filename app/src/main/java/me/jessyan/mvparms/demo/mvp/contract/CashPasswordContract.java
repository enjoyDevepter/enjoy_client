package me.jessyan.mvparms.demo.mvp.contract;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.request.VeritfyRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.SetCashPasswordRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.SetCashPasswordResponse;


public interface CashPasswordContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void showOk();
        void showVerity();
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<SetCashPasswordResponse> setCashPassword(SetCashPasswordRequest request);
        Observable<BaseResponse> getVerifyForFind(VeritfyRequest veritfyRequest);
    }
}
