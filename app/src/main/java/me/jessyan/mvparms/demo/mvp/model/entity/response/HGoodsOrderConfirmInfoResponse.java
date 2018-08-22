package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.HAppointments;
import me.jessyan.mvparms.demo.mvp.model.entity.Coupon;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;

/**
 * Created by guomin on 2018/7/28.
 */

public class HGoodsOrderConfirmInfoResponse extends BaseResponse {
    private long balance;
    private long deductionMoney;
    private long freight;
    private long money;
    private long price;
    private long totalPrice;
    private long payMoney;
    private long coupon;
    private String couponId;
    private List<Coupon> couponList;
    private List<Goods> goodsList;
    private List<HAppointments> appointmentsDateList;

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getDeductionMoney() {
        return deductionMoney;
    }

    public void setDeductionMoney(long deductionMoney) {
        this.deductionMoney = deductionMoney;
    }

    public long getFreight() {
        return freight;
    }

    public void setFreight(long freight) {
        this.freight = freight;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public long getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(long payMoney) {
        this.payMoney = payMoney;
    }

    public long getCoupon() {
        return coupon;
    }

    public void setCoupon(long coupon) {
        this.coupon = coupon;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public List<Coupon> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<Coupon> couponList) {
        this.couponList = couponList;
    }


    public List<HAppointments> getAppointmentsDateList() {
        return appointmentsDateList;
    }

    public void setAppointmentsDateList(List<HAppointments> appointmentsDateList) {
        this.appointmentsDateList = appointmentsDateList;
    }

    @Override
    public String toString() {
        return "HGoodsOrderConfirmInfoResponse{" +
                "balance=" + balance +
                ", deductionMoney=" + deductionMoney +
                ", freight=" + freight +
                ", money=" + money +
                ", price=" + price +
                ", totalPrice=" + totalPrice +
                ", payMoney=" + payMoney +
                ", coupon=" + coupon +
                ", couponId='" + couponId + '\'' +
                ", couponList=" + couponList +
                ", goodsList=" + goodsList +
                ", appointmentsDateList=" + appointmentsDateList +
                '}';
    }
}
