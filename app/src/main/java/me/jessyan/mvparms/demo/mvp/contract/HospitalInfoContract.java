package me.jessyan.mvparms.demo.mvp.contract;

import android.app.Activity;

import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;

import java.util.List;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.DoctorListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.DoctorListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean.ActivityInfo;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean.HospitalInfoBean;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.request.ActivityInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.request.HospitalInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.request.LoginUserHospitalInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.response.ActivityInfoListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.response.HospitalInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.response.LoginUserHospitalInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.request.GoodsListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HGoodsListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.FollowRequest;


public interface HospitalInfoContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        Activity getActivity();

        void updateActivityInfo(List<ActivityInfo> activityInfoList);

        void updateHosptialInfo(HospitalInfoBean hospital);

        void endDoctor(boolean isDoctorEnd);

        void endGoods(boolean isGoodsEnd);

        void endLoadDoctorMore();

        void startLoadDoctorMore();

        void endLoadGoodsMore();

        void startLoadGoodsMore();

        void hideDoctorLoading();

        void hideGoodsLoading();

        Cache getCache();

        void updatefollowStatus(boolean follow);

    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<LoginUserHospitalInfoResponse> requestHospitalByUser(LoginUserHospitalInfoRequest request);

        Observable<HospitalInfoResponse> requestHospital(HospitalInfoRequest request);

        Observable<DoctorListResponse> requestDoctorPage(DoctorListRequest request);

        Observable<HGoodsListResponse> getHGoodsList(GoodsListRequest request);

        Observable<BaseResponse> follow(FollowRequest request);

        Observable<ActivityInfoListResponse> getActivityList(ActivityInfoRequest request);

    }
}
