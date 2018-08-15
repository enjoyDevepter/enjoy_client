/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.jessyan.mvparms.demo.mvp.model.api.service;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.request.GoodsDetailsRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.GoodsListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.HGoodsOrderConfirmInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.HGoodsPayOrderRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HGoodsDetailsResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HGoodsListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HGoodsOrderConfirmInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HGoodsPayOrderResponse;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * ================================================
 * 展示 {@link Retrofit#create(Class)} 中需要传入的 ApiService 的使用方式
 * 存放关于用户的一些 API
 * <p>
 * Created by JessYan on 08/05/2016 12:05
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public interface HGoodsService {


    @POST("gateway")
    Observable<HGoodsListResponse> getHGoodsList(@Body GoodsListRequest request);

    @POST("gateway")
    Observable<HGoodsDetailsResponse> getHGoodsDetails(@Body GoodsDetailsRequest request);

    @POST("gateway")
    Observable<HGoodsOrderConfirmInfoResponse> getOrderConfirmInfo(@Body HGoodsOrderConfirmInfoRequest request);

    @POST("gateway")
    Observable<HGoodsPayOrderResponse> placeHGoodsOrder(@Body HGoodsPayOrderRequest request);

}
