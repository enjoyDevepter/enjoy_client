package me.jessyan.mvparms.demo.mvp.model.api.service;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.CommentDoctorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.GetDoctorCommentReplyPageRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.ReplyDoctorCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.CommentDoctorResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.DoctorAllCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.DoctorAllCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.DoctorHonorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.DoctorHonorResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.DoctorHotCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.DoctorHotCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.DoctorInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.DoctorInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.DoctorIntorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.DoctorIntorResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.DoctorListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.DoctorListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.DoctorPaperRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.DoctorPaperResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.LikeDoctorCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.GetDoctorCommentReplyPageResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.LikeDoctorCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.LikeDoctorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.LikeDoctorResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.LoginUserDoctorAllCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.LoginUserDoctorAllCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.LoginUserDoctorHotCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.LoginUserDoctorHotCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.LoginUserDoctorInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.LoginUserDoctorInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.UnLikeDoctorCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.ReplyDoctorCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.UnLikeDoctorCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.UnLikeDoctorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.UnLikeDoctorResponse;
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


    @POST("gateway")
    // 未登陆用户请求全部评论
    Observable<DoctorAllCommentResponse> requestAllComment(@Body DoctorAllCommentRequest request);

    @POST("gateway")
    // 登陆用户请求全部评论
    Observable<LoginUserDoctorAllCommentResponse> loginUserRequestAllComment(@Body LoginUserDoctorAllCommentRequest request);

    @POST("gateway")
    // 点赞医生评论
    Observable<LikeDoctorCommentResponse> likeDoctorComment(@Body LikeDoctorCommentRequest request);

    @POST("gateway")
    // 取消点赞医生评论
    Observable<UnLikeDoctorCommentResponse> unLikeDoctorComment(@Body UnLikeDoctorCommentRequest request);


    @POST("gateway")
    // 评论医生评论
    Observable<ReplyDoctorCommentResponse> replyDoctorComment(@Body ReplyDoctorCommentRequest request);


    @POST("gateway")
    // 获取医生评论的回复列表
    Observable<GetDoctorCommentReplyPageResponse> getDoctorCommentReply(@Body GetDoctorCommentReplyPageRequest request);

}
