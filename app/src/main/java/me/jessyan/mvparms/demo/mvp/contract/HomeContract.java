package me.jessyan.mvparms.demo.mvp.contract;

import android.app.Activity;

import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.Ad;
import me.jessyan.mvparms.demo.mvp.model.entity.Module;
import me.jessyan.mvparms.demo.mvp.model.entity.NaviInfo;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryVoteRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.FollowMemberRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.HomeRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HomeResponse;


public interface HomeContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        Activity getActivity();

        void startLoadMore();

        void endLoadMore();

        void setLoadedAllItems(boolean has);

        Cache getCache();

        //申请权限
        RxPermissions getRxPermissions();

        void refreshUI(List<NaviInfo> firstNavList, List<Ad> ads, List<Module> moduleList, List<NaviInfo> secondNavList);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {

        Observable<HomeResponse> getHomeInfo(HomeRequest request);

        Observable<DiaryListResponse> getDiaryList(DiaryListRequest request);

        Observable<BaseResponse> diaryVote(DiaryVoteRequest request);

        Observable<BaseResponse> follow(FollowMemberRequest request);

    }
}
