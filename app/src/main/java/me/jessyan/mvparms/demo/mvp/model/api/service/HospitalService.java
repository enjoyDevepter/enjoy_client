package me.jessyan.mvparms.demo.mvp.model.api.service;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.request.HospitalInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.request.HospitalListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.request.LoginUserHospitalInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.response.HospitalInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.response.HospitalListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.response.HospitalResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.response.LoginUserHospitalInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.FollowRequest;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface HospitalService {

    @POST("gateway")
    Observable<HospitalListResponse> requestHospitalPage(@Body HospitalListRequest request);

    @POST("gateway")
        // 未登录用户获取医院详情
    Observable<HospitalInfoResponse> requestHospitalInfo(@Body HospitalInfoRequest request);

    @POST("gateway")
        // 已登录用户获取医院详情
    Observable<LoginUserHospitalInfoResponse> requestHospitalInfoForLoginUser(@Body LoginUserHospitalInfoRequest request);

    @POST("gateway")
    Observable<HospitalResponse> getHospitalList(@Body HospitalListRequest request);

    @POST("gateway")
    Observable<BaseResponse> follow(@Body FollowRequest request);

}
