package me.jessyan.mvparms.demo.mvp.model.api.service;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.diary.DiaryCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.request.HospitalListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.response.HospitalListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.order.request.OrderOperationRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.order.response.LogisticsResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.pay.request.PayInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.pay.request.PayRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.pay.response.PayInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.pay.response.PayResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.request.AddGoodsToCartRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.CartListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.CollectGoodsRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DeleteCartListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryCommentListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryDetailsRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryForGoodsRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryImagesRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryVoteRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.EidtCartRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.FollowMemberRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.GoodsDetailsRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.GoodsListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.HomeADRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.HomeRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.HotRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.MealDetailsRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.MealListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.MealOrderConfrimRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.OrderConfirmInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.OrderDetailsRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.OrderRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.PayMealOrderRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.PayOrderRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.ReleaseDiaryRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.ShareRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.SimpleRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.StoresListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.CartListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.CategoryResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.CityResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryApplyResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryCommentListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryDetailsResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryImagesResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryNaviListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryTypeListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.GoodsDetailsResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.GoodsListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HomeAdResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HomeResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HotResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.MealDetailsResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.MealListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.MealOrderConfirmResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.OrderConfirmInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.OrderDetailsResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.OrderResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.PayMealOrderResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.PayOrderResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.StoresListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.AuthenticationRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.LocationRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.MyDiaryRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.UpdateRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.ShareResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.UpdateResponse;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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
    Observable<GoodsListResponse> getGoodsList(@Body GoodsListRequest request);


    @POST("gateway")
    Observable<GoodsDetailsResponse> getGoodsDetails(@Body GoodsDetailsRequest request);

    @POST("gateway")
    Observable<StoresListResponse> getStores(@Body StoresListRequest request);

    @POST("gateway")
    Observable<HospitalListResponse> getHospitals(@Body HospitalListRequest request);

    @POST("gateway")
    Observable<CartListResponse> getGoodsOfCart(@Body CartListRequest request);

    @POST("gateway")
    Observable<CartListResponse> editCartList(@Body EidtCartRequest request);

    @POST("gateway")
    Observable<CartListResponse> deleteCartList(@Body DeleteCartListRequest request);

    @POST("gateway")
    Observable<BaseResponse> addGoodsToCart(@Body AddGoodsToCartRequest request);

    @POST("gateway")
    Observable<BaseResponse> collectGoods(@Body CollectGoodsRequest request);

    @POST("gateway")
    Observable<OrderConfirmInfoResponse> getOrderConfirmInfo(@Body OrderConfirmInfoRequest request);

    @POST("gateway")
    Observable<PayOrderResponse> placeOrder(@Body PayOrderRequest request);

    @POST("gateway")
    Observable<MealListResponse> getTaoCanList(@Body MealListRequest request);

    @POST("gateway")
    Observable<MealDetailsResponse> getMealDetail(@Body MealDetailsRequest request);

    @POST("gateway")
    Observable<BaseResponse> collectGoods(@Body MealDetailsRequest request);

    @POST("gateway")
    Observable<MealOrderConfirmResponse> getMealOrderConfirmInfo(@Body MealOrderConfrimRequest request);

    @POST("gateway")
    Observable<PayMealOrderResponse> placeMealOrder(@Body PayMealOrderRequest request);

    @POST("gateway")
    Observable<DiaryTypeListResponse> getDiaryType(@Body SimpleRequest request);

    @Multipart
    @POST("file/imageUpload")
    Observable<BaseResponse> uploadImage(@Part("type") String description, @Part MultipartBody.Part file);

    @POST("gateway")
    Observable<BaseResponse> releaseDiary(@Body ReleaseDiaryRequest request);

    @POST("gateway")
    Observable<DiaryNaviListResponse> getDiaryNaviType(@Body SimpleRequest request);

    @POST("gateway")
    Observable<DiaryListResponse> getDiaryList(@Body DiaryListRequest request);

    @POST("gateway")
    Observable<DiaryApplyResponse> apply(@Body DiaryRequest request);

    @POST("gateway")
    Observable<DiaryListResponse> getDiaryForGoodsIdList(@Body DiaryForGoodsRequest request);

    @POST("gateway")
    Observable<BaseResponse> diaryVote(@Body DiaryVoteRequest request);

    @POST("gateway")
    Observable<BaseResponse> follow(@Body FollowMemberRequest request);

    @POST("gateway")
    Observable<BaseResponse> comment(@Body DiaryCommentRequest request);

    @POST("gateway")
    Observable<DiaryResponse> getDiary(@Body DiaryRequest request);

    @POST("gateway")
    Observable<DiaryImagesResponse> getDiaryImages(@Body DiaryImagesRequest request);

    @POST("gateway")
    Observable<DiaryListResponse> getMyDiaryList(@Body MyDiaryRequest request);

    @POST("gateway")
    Observable<DiaryDetailsResponse> getDiaryDetails(@Body DiaryDetailsRequest request);

    @POST("gateway")
    Observable<DiaryCommentListResponse> getDiaryComment(@Body DiaryCommentListRequest request);

    @POST("gateway")
    Observable<OrderResponse> getMyOrder(@Body OrderRequest request);

    @POST("gateway")
    Observable<OrderDetailsResponse> getOrderDetails(@Body OrderDetailsRequest request);

    @POST("gateway")
    Observable<BaseResponse> auth(@Body AuthenticationRequest request);

    @POST("gateway")
    Observable<BaseResponse> cancelOrder(@Body OrderOperationRequest request);

    @POST("gateway")
    Observable<UpdateResponse> checkUpdate(@Body UpdateRequest request);

    @POST("gateway")
    Observable<CityResponse> getAreaForLoaction(@Body LocationRequest request);

    @POST("gateway")
    Observable<HomeAdResponse> getOrCancelAD(@Body HomeADRequest request);

    @POST("gateway")
    Observable<PayInfoResponse> getOrderPayInfo(@Body PayInfoRequest request);

    @POST("gateway")
    Observable<PayInfoResponse> getPayStatus(@Body PayInfoRequest request);

    @POST("gateway")
    Observable<PayResponse> pay(@Body PayRequest request);

    @POST("gateway")
    Observable<LogisticsResponse> getLogistics(@Body OrderOperationRequest request);

    @POST("gateway")
    Observable<ShareResponse> share(@Body ShareRequest request);

    @POST("gateway")
    Observable<BaseResponse> getTel(@Body SimpleRequest request);

}
