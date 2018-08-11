package me.jessyan.mvparms.demo.mvp.model.api.service;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DoctorListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DoctorListResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface HospitalService {

    @POST("gateway")
    Observable<DoctorListResponse> requestDoctorPage(@Body DoctorListRequest request);
}
