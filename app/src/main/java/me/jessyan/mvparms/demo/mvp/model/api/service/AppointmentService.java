package me.jessyan.mvparms.demo.mvp.model.api.service;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.request.AppointmentAndMealRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.GetAppointmentTimeRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.ModifyAppointmentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.AppointmentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.GetAppointmentTimeResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.MyMealResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by guomin on 2018/1/29.
 */

public interface AppointmentService {

    @POST("gateway")
    Observable<AppointmentResponse> getAppointment(@Body AppointmentAndMealRequest request);

    @POST("gateway")
    Observable<MyMealResponse> getMyMealAppointment(@Body AppointmentAndMealRequest request);

    @POST("gateway")
    Observable<GetAppointmentTimeResponse> getAppointmentTime(@Body GetAppointmentTimeRequest request);

    @POST("gateway")
    Observable<BaseResponse> modifyAppointmentTime(@Body ModifyAppointmentRequest request);

}
