package me.jessyan.mvparms.demo.mvp.contract;

import android.app.Activity;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.CommentDoctorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.CommentDoctorResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorBean;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorHotCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorHotCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LikeDoctorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LikeDoctorResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LoginUserDoctorHotCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LoginUserDoctorHotCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LoginUserDoctorInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LoginUserDoctorInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.UnLikeDoctorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.UnLikeDoctorResponse;


public interface DoctorMainContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        Activity getActivity();
        void updateDoctorInfo(DoctorBean doctorBean);
        void updateLikeImage(boolean isLike);
        void commentOk();
        void endLoadMore();
        void setEnd(boolean isEnd);
        void startLoadMore();
        void updateRecyclerViewHeight();
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<DoctorInfoResponse> requestDoctorInfo(DoctorInfoRequest request);
        Observable<LoginUserDoctorInfoResponse> requestLoginUserDoctorInfo(LoginUserDoctorInfoRequest request);
        Observable<LikeDoctorResponse> likeDoctor(LikeDoctorRequest request);
        Observable<UnLikeDoctorResponse> unlikeDoctor(UnLikeDoctorRequest request);
        Observable<CommentDoctorResponse> commentDoctor(CommentDoctorRequest request);
        Observable<LoginUserDoctorHotCommentResponse> loginUserRequestDoctorHotComment(LoginUserDoctorHotCommentRequest request);
        Observable<DoctorHotCommentResponse> requestDoctorHotComment(DoctorHotCommentRequest request);
    }
}
