package me.jessyan.mvparms.demo.mvp.model.entity.user.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.Coupon;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;

public class MyCouponListResponse extends BaseResponse {
    private int nextPageIndex;
    private List<Coupon> couponList;

    public int getNextPageIndex() {
        return nextPageIndex;
    }

    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }

    public List<Coupon> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<Coupon> couponList) {
        this.couponList = couponList;
    }

    @Override
    public String toString() {
        return "MyCouponListResponse{" +
                "nextPageIndex=" + nextPageIndex +
                ", couponList=" + couponList +
                '}';
    }
}
