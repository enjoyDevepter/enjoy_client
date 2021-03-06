package me.jessyan.mvparms.demo.mvp.contract;

import android.app.Activity;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.Share;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.FollowRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.GetMyMemberListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.GetMyMemberListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.ShareResponse;


public interface InviteMainContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void updateUrl(String url);
        void setEnd(boolean isEnd);
        void showError(boolean hasDate);

        void endLoadMore();

        void startLoadMore();

        Activity getActivity();

        void showWX(Share share);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<GetMyMemberListResponse> getMyMemberList(GetMyMemberListRequest request);

        Observable<ShareResponse> share(FollowRequest request);
    }
}
