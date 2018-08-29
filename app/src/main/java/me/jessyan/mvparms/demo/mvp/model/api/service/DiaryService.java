package me.jessyan.mvparms.demo.mvp.model.api.service;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.diary.ProjectRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.GoodsListResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DiaryService {

    @POST("gateway")
        // 请求项目列表
    Observable<GoodsListResponse> getProjects(@Body ProjectRequest request);

}
