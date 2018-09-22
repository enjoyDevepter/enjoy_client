package me.jessyan.mvparms.demo.mvp.contract;

import android.app.Activity;

import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.diary.DiaryCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryCommentListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryDetailsRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryVoteRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.FollowMemberRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryCommentListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryDetailsResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.FollowRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.ShareResponse;


public interface DiaryDetailsContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        Activity getActivity();

        Cache getCache();

        void updateUI(DiaryDetailsResponse response);

        void updatefollowStatus(boolean follow);

        void updateVoteStatus(boolean vote);

        void comment(boolean success);

    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {

        Observable<DiaryDetailsResponse> getDiaryDetails(DiaryDetailsRequest request);

        Observable<DiaryCommentListResponse> getDiaryComment(DiaryCommentListRequest request);

        Observable<BaseResponse> diaryVote(DiaryVoteRequest request);

        Observable<BaseResponse> follow(FollowMemberRequest request);

        Observable<BaseResponse> comment(DiaryCommentRequest request);

        Observable<ShareResponse> share(FollowRequest request);

    }
}
