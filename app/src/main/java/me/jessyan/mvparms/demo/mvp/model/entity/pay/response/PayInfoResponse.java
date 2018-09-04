package me.jessyan.mvparms.demo.mvp.model.entity.pay.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.PayEntry;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;

/**
 * Created by guomin on 2018/7/28.
 */

public class PayInfoResponse extends BaseResponse {
    private String orderId;
    private long orderTime;
    private long payMoney;
    private long totalPrice;
    private String orderType;
    private String payType;
    private String payTypeDesc;
    private String payStatus;
    private List<PayEntry> payEntryList;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public long getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(long payMoney) {
        this.payMoney = payMoney;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayTypeDesc() {
        return payTypeDesc;
    }

    public void setPayTypeDesc(String payTypeDesc) {
        this.payTypeDesc = payTypeDesc;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public List<PayEntry> getPayEntryList() {
        return payEntryList;
    }

    public void setPayEntryList(List<PayEntry> payEntryList) {
        this.payEntryList = payEntryList;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    @Override
    public String toString() {
        return "HGoodsPayOrderResponse{" +
                "orderId='" + orderId + '\'' +
                ", orderTime=" + orderTime +
                ", payMoney=" + payMoney +
                ", totalPrice=" + totalPrice +
                ", orderType='" + orderType + '\'' +
                ", payType='" + payType + '\'' +
                ", payTypeDesc='" + payTypeDesc + '\'' +
                ", payStatus='" + payStatus + '\'' +
                ", payEntryList=" + payEntryList +
                '}';
    }
}



