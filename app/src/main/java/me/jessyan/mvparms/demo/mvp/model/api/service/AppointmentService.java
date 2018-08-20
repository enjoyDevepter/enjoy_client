package me.jessyan.mvparms.demo.mvp.model.api.service;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.request.AppointmentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.AppointmentResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by guomin on 2018/1/29.
 */

public interface AppointmentService {

    @POST("gateway")
    Observable<AppointmentResponse> getAppointment(@Body AppointmentRequest request);

    @POST("gateway")
    Observable<AppointmentResponse> getMyMealAppointment(@Body AppointmentRequest request);

}
