package me.jessyan.mvparms.demo.mvp.model.api.service;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.request.ForgetRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.LoginByPhoneRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.LoginByUserRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.ModifyRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.RegisterRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.VeritfyRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.RegisterResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by guomin on 2018/1/29.
 */

public interface LoginAndRegisterService {

    @POST("gateway")
    Observable<RegisterResponse> loginByPhone(@Body LoginByPhoneRequest loginByPhoneRequest);

    @POST("gateway")
    Observable<RegisterResponse> loginByUser(@Body LoginByUserRequest loginByUserRequest);

    @POST("gateway")
    Observable<RegisterResponse> register(@Body RegisterRequest registerRequest);

    @POST("gateway")
    Observable<BaseResponse> getVerify(@Body VeritfyRequest veritfyRequest);

    @POST("gateway")
    Observable<BaseResponse> getVerifyForUser(@Body VeritfyRequest veritfyRequest);

    @POST("gateway")
    Observable<BaseResponse> getVerifyForFind(@Body VeritfyRequest veritfyRequest);

    @POST("gateway")
    Observable<RegisterResponse> find(@Body ForgetRequest forgetRequest);

    @POST("gateway")
    Observable<BaseResponse> modify(@Body ModifyRequest modifyRequest);

}
