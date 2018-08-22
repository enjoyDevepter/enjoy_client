package me.jessyan.mvparms.demo.mvp.model.entity.request;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.Goods;

/**
 * Created by guomin on 2018/7/25.
 */

public class OrderConfirmInfoRequest extends BaseRequest {

    private int cmd = 503;
    private String province;
    private String city;
    private String county;
    private String deliveryMethodId;
    private long money;
    private String couponId;
    private String token;
    private List<Goods> goodsList;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    @Override
    public String toString() {
        return "OrderConfirmInfoRequest{" +
                "cmd=" + cmd +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", county='" + county + '\'' +
                ", deliveryMethodId='" + deliveryMethodId + '\'' +
                ", money=" + money +
                ", couponId='" + couponId + '\'' +
                ", token='" + token + '\'' +
                ", goodsList=" + goodsList +
                '}';
    }
}