package me.jessyan.mvparms.demo.mvp.model.api.service;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.request.HospitalListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.response.HospitalListResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface HospitalService {

    @POST("gateway")
    Observable<HospitalListResponse> requestHospitalPage(@Body HospitalListRequest request);
}
