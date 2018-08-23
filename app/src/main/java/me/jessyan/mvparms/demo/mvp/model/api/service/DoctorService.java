package me.jessyan.mvparms.demo.mvp.model.api.service;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.CommentDoctorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.CommentDoctorResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorHonorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorHonorResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorHotCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorHotCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorIntorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorIntorResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorPaperRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorPaperResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LikeDoctorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LikeDoctorResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LoginUserDoctorHotCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LoginUserDoctorHotCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LoginUserDoctorInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LoginUserDoctorInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.UnLikeDoctorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.UnLikeDoctorResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DoctorService {

    @POST("gateway")
    // 请求医生列表
    Observable<DoctorListResponse> requestDoctorPage(@Body DoctorListRequest request);

    @POST("gateway")
    // 未登录用户请求医生详情
    Observable<DoctorInfoResponse> requestDoctorInfo(@Body DoctorInfoRequest request);

    @POST("gateway")
    // 已登录用户请求医生详情
    Observable<LoginUserDoctorInfoResponse> requestLoginUserDoctorInfo(@Body LoginUserDoctorInfoRequest request);

    @POST("gateway")
    // 点赞医生
    Observable<LikeDoctorResponse> likeDoctor(@Body LikeDoctorRequest request);

    @POST("gateway")
    // 取消点赞医生
    Observable<UnLikeDoctorResponse> unlikeDoctor(@Body UnLikeDoctorRequest request);

    @POST("gateway")
    // 获取医生详情
    Observable<DoctorIntorResponse> getDoctorIntor(@Body DoctorIntorRequest request);

    @POST("gateway")
    // 获取医生证书列表
    Observable<DoctorPaperResponse> getDoctorPaper(@Body DoctorPaperRequest request);

    @POST("gateway")
    // 获取医生荣誉列表
    Observable<DoctorHonorResponse> getDoctorHonor(@Body DoctorHonorRequest request);

    @POST("gateway")
    // 评论医生
    Observable<CommentDoctorResponse> commentDoctor(@Body CommentDoctorRequest request);

    @POST("gateway")
    // 未登陆用户请求热门评论
    Observable<DoctorHotCommentResponse> requestHotComment(@Body DoctorHotCommentRequest request);


    @POST("gateway")
        // 登陆用户请求热门评论
    Observable<LoginUserDoctorHotCommentResponse> loginUserRequestHotComment(@Body LoginUserDoctorHotCommentRequest request);

}
