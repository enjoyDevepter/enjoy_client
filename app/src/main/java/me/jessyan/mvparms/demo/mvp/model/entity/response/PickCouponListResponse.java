package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.PickCoupon;

/**
 * Created by guomin on 2018/7/28.
 */

public class PickCouponListResponse extends BaseResponse {
    private List<PickCoupon> couponList;
    private String imageUrl;

    public List<PickCoupon> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<PickCoupon> couponList) {
        this.couponList = couponList;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "PickCouponListResponse{" +
                "couponList=" + couponList +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
