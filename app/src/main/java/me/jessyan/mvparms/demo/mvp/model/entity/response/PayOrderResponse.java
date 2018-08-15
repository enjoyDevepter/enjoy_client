package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.PayEntry;
import me.jessyan.mvparms.demo.mvp.model.entity.PayGoods;

/**
 * Created by guomin on 2018/7/28.
 */

public class PayOrderResponse extends BaseResponse {

    private String orderId;
    private long orderTime;
    private long payMoney;
    private String payStatus;
    private List<PayGoods> goodsList;
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

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public List<PayGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<PayGoods> goodsList) {
        this.goodsList = goodsList;
    }

    public List<PayEntry> getPayEntryList() {
        return payEntryList;
    }

    public void setPayEntryList(List<PayEntry> payEntryList) {
        this.payEntryList = payEntryList;
    }

    @Override
    public String toString() {
        return "PayOrderResponse{" +
                "orderId='" + orderId + '\'' +
                ", orderTime=" + orderTime +
                ", payMoney=" + payMoney +
                ", payStatus='" + payStatus + '\'' +
                ", goodsList=" + goodsList +
                ", payEntryList=" + payEntryList +
                '}';
    }

}



