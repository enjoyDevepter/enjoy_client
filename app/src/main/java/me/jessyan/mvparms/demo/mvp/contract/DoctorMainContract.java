package me.jessyan.mvparms.demo.mvp.contract;

import android.app.Activity;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.CommentDoctorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.CommentDoctorResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorBean;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.DoctorHotCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.DoctorHotCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.DoctorInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.DoctorInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.LikeDoctorCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.LikeDoctorCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.LikeDoctorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.LikeDoctorResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.LoginUserDoctorHotCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.LoginUserDoctorHotCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.LoginUserDoctorInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.LoginUserDoctorInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.UnLikeDoctorCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.UnLikeDoctorCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.UnLikeDoctorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.UnLikeDoctorResponse;


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
        Observable<LikeDoctorCommentResponse> likeDoctorComment(LikeDoctorCommentRequest request);
        Observable<UnLikeDoctorCommentResponse> unLikeDoctorComment(UnLikeDoctorCommentRequest request);
    }
}
