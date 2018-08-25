package me.jessyan.mvparms.demo.mvp.contract;

import android.app.Activity;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.DoctorAllCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.DoctorAllCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.LikeDoctorCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.LikeDoctorCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.LoginUserDoctorAllCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.LoginUserDoctorAllCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.UnLikeDoctorCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.UnLikeDoctorCommentResponse;


public interface DoctorAllCommentContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void setEnd(boolean isEnd);
        void endLoadMore();
        void startLoadMore();
        Activity getActivity();
        void hideLoading();
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<DoctorAllCommentResponse> requestAllComment(DoctorAllCommentRequest request);
        Observable<LoginUserDoctorAllCommentResponse> loginUserRequestAllComment(LoginUserDoctorAllCommentRequest request);
        Observable<LikeDoctorCommentResponse> likeDoctorComment(LikeDoctorCommentRequest request);
        Observable<UnLikeDoctorCommentResponse> unLikeDoctorComment(UnLikeDoctorCommentRequest request);
    }
}
