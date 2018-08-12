package me.jessyan.mvparms.demo.mvp.model.entity.request;

import java.util.List;

/**
 * Created by guomin on 2018/7/25.
 */

public class PayOrderRequest extends BaseRequest {
    private int cmd = 504;
    private String memberAddressId;
    private String storeId;
    private String deliveryMethodId;
    private String couponId;
    private long coupon;
    private long deductionMoney;
    private long money;
    private long freight;
    private long price;
    private long totalPrice;
    private long payMoney;
    private String remark;
    private String token;
    private List<OrderConfirmInfoRequest.OrderGoods> goodsList;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getMemberAddressId() {
        return memberAddressId;
    }

    public void setMemberAddressId(String memberAddressId) {
        this.memberAddressId = memberAddressId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getDeliveryMethodId() {
        return deliveryMethodId;
    }

    public void setDeliveryMethodId(String deliveryMethodId) {
        this.deliveryMethodId = deliveryMethodId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public long getCoupon() {
        return coupon;
    }

    public void setCoupon(long coupon) {
        this.coupon = coupon;
    }

    public long getDeductionMoney() {
        return deductionMoney;
    }

    public void setDeductionMoney(long deductionMoney) {
        this.deductionMoney = deductionMoney;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public long getFreight() {
        return freight;
    }

    public void setFreight(long freight) {
        this.freight = freight;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<OrderConfirmInfoRequest.OrderGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<OrderConfirmInfoRequest.OrderGoods> goodsList) {
        this.goodsList = goodsList;
    }

    @Override
    public String toString() {
        return "PayOrderRequest{" +
                "cmd=" + cmd +
                ", memberAddressId='" + memberAddressId + '\'' +
                ", storeId='" + storeId + '\'' +
                ", deliveryMethodId='" + deliveryMethodId + '\'' +
                ", couponId='" + couponId + '\'' +
                ", coupon=" + coupon +
                ", deductionMoney=" + deductionMoney +
                ", money=" + money +
                ", freight=" + freight +
                ", price=" + price +
                ", totalPrice=" + totalPrice +
                ", payMoney=" + payMoney +
                ", remark='" + remark + '\'' +
                ", token='" + token + '\'' +
                ", goodsList=" + goodsList +
                '}';
    }
}