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

import java.util.List;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.User;
import me.jessyan.mvparms.demo.mvp.model.entity.request.AddressListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DelAddressRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.ModifyAddressRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.SimpleRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.AddressListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.AllAddressResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.AddBankCardRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.DelBankCardRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.GetAllBankCardListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.GetCashCoinRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.GetConsumeInfoPageRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.QiandaoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.AddBankCardResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.DelBankCardResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.GetAllBankCardListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.GetCashCoinResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.GetConsumeInfoPageResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.QiandaoResponse;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

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
public interface UserService {
    String HEADER_API_VERSION = "Accept: application/vnd.github.v3+json";

    @Headers({HEADER_API_VERSION})
    @GET("/users")
    Observable<List<User>> getUsers(@Query("since") int lastIdQueried, @Query("per_page") int perPage);

    @POST("gateway")
    Observable<AddressListResponse> getAddressList(@Body AddressListRequest request);

    @POST("gateway")
    Observable<BaseResponse> modifyAddress(@Body ModifyAddressRequest request);

    @POST("gateway")
    Observable<BaseResponse> delAddrss(@Body DelAddressRequest request);

    @POST("gateway")
    Observable<AllAddressResponse> getAllAddressList(@Body SimpleRequest request);

    @POST("gateway")
    // 获取银行卡列表
    Observable<GetAllBankCardListResponse> getAllBankCard(@Body GetAllBankCardListRequest request);

    @POST("gateway")
    // 添加银行卡
    Observable<AddBankCardResponse> addBankCard(@Body AddBankCardRequest request);

    @POST("gateway")
    // 删除银行卡
    Observable<DelBankCardResponse> delBankCard(@Body DelBankCardRequest request);

    @POST("gateway")
    // 获取消费币详情
    Observable<GetConsumeInfoPageResponse> getConsumeInfoPage(@Body GetConsumeInfoPageRequest request);

    @POST("gateway")
    // 签到
    Observable<QiandaoResponse> qiandao(@Body QiandaoRequest request);

    @POST("gateway")
        // 签到
    Observable<GetCashCoinResponse> getCashCoin(@Body GetCashCoinRequest request);

}
