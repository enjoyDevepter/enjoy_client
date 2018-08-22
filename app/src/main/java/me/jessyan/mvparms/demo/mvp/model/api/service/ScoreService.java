package me.jessyan.mvparms.demo.mvp.model.api.service;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.CommentDoctorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.CommentDoctorResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.score.UserScorePageRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.score.UserScorePageResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ScoreService {

    @POST("gateway")
        // 请求积分详情
    Observable<UserScorePageResponse> requestScorePage(@Body UserScorePageRequest request);
}
