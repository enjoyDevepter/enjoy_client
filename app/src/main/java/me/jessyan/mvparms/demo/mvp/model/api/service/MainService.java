package me.jessyan.mvparms.demo.mvp.model.api.service;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.request.AddGoodsToCartRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.CartListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DeleteCartListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.EidtCartRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.GoodsDetailsRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.GoodsListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.HomeRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.HotRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.OrderConfirmInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.PayOrderRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.SimpleRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.StoresListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.CartListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.CategoryResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.CityResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.GoodsDetailsResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.GoodsListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HomeResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HotResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.OrderConfirmInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.PayOrderResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.StoresListResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by guomin on 2018/1/29.
 */

public interface MainService {

    @POST("gateway")
    Observable<CityResponse> getCities(@Body SimpleRequest request);


    @POST("gateway")
    Observable<HotResponse> getHot(@Body HotRequest request);

    @POST("gateway")
    Observable<CategoryResponse> getCategory(@Body SimpleRequest request);

    @POST("gateway")
    Observable<HomeResponse> getHomeInfo(@Body HomeRequest request);

    @POST("gateway")
    Observable<GoodsListResponse> getCategory(@Body GoodsListRequest request);


    @POST("gateway")
    Observable<GoodsDetailsResponse> getGoodsDetails(@Body GoodsDetailsRequest request);

    @POST("gateway")
    Observable<StoresListResponse> getStores(@Body StoresListRequest request);

    @POST("gateway")
    Observable<CartListResponse> getGoodsOfCart(@Body CartListRequest request);

    @POST("gateway")
    Observable<CartListResponse> editCartList(@Body EidtCartRequest request);

    @POST("gateway")
    Observable<CartListResponse> deleteCartList(@Body DeleteCartListRequest request);

    @POST("gateway")
    Observable<BaseResponse> addGoodsToCart(@Body AddGoodsToCartRequest request);

    @POST("gateway")
    Observable<OrderConfirmInfoResponse> getOrderConfirmInfo(@Body OrderConfirmInfoRequest request);

    @POST("gateway")
    Observable<PayOrderResponse> placeOrder(@Body PayOrderRequest request);


}
