package me.jessyan.mvparms.demo.mvp.contract;

import android.app.Activity;

import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryVoteRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.FollowMemberRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.MyDiaryRequest;


public interface DiaryForGoodsContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        Activity getActivity();

        Cache getCache();

        void updateUI(DiaryResponse response);

        void updateFollow(boolean follow);

        void startLoadMore();

        void endLoadMore();

        void setLoadedAllItems(boolean has);

        void updateDiaryUI(int count);

    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {

        Observable<DiaryResponse> getDiary(DiaryRequest request);

        Observable<DiaryListResponse> getMyDiaryList(MyDiaryRequest request);

        Observable<BaseResponse> diaryVote(DiaryVoteRequest request);

        Observable<BaseResponse> follow(FollowMemberRequest request);

    }
}
