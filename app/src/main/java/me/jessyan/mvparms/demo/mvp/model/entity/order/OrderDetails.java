package me.jessyan.mvparms.demo.mvp.model.entity.order;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.Address;
import me.jessyan.mvparms.demo.mvp.model.entity.Hospital;
import me.jessyan.mvparms.demo.mvp.model.entity.MealGoods;
import me.jessyan.mvparms.demo.mvp.model.entity.Store;

/**
 * Created by guomin on 2018/8/17.
 */

public class OrderDetails {
    private long coupon;
    private long deductionMoney;
    private String deliveryMethodDesc;
    private String deliveryMethodId;
    private long freight;
    private long money;
    private int nums;
    private String appointmentsDate;
    private String appointmentsTime;
    private String orderId;
    private String orderStatus;
    private String orderStatusDesc;
    private String payType;
    private String payTypeDesc;
    private String remark;
    private long orderTime;
    private long payMoney;
    private long payTime;
    private long price;
    private long totalPrice;
    private List<OrderGoods> goodsList;
    private List<MealGoods> setMealGoodsList;
    private Address orderRecipientInfo;
    private Hospital hospital;
    private Store store;

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

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatusDesc() {
        return orderStatusDesc;
    }

    public void setOrderStatusDesc(String orderStatusDesc) {
        this.orderStatusDesc = orderStatusDesc;
    }

    public String getAppointmentsDate() {
        return appointmentsDate;
    }

    public void setAppointmentsDate(String appointmentsDate) {
        this.appointmentsDate = appointmentsDate;
    }

    public String getAppointmentsTime() {
        return appointmentsTime;
    }

    public void setAppointmentsTime(String appointmentsTime) {
        this.appointmentsTime = appointmentsTime;
    }

    public List<MealGoods> getSetMealGoodsList() {
        return setMealGoodsList;
    }

    public void setSetMealGoodsList(List<MealGoods> setMealGoodsList) {
        this.setMealGoodsList = setMealGoodsList;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public long getPayTime() {
        return payTime;
    }

    public void setPayTime(long payTime) {
        this.payTime = payTime;
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

    public List<OrderGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<OrderGoods> goodsList) {
        this.goodsList = goodsList;
    }

    public Address getOrderRecipientInfo() {
        return orderRecipientInfo;
    }

    public void setOrderRecipientInfo(Address orderRecipientInfo) {
        this.orderRecipientInfo = orderRecipientInfo;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public String getDeliveryMethodDesc() {
        return deliveryMethodDesc;
    }

    public void setDeliveryMethodDesc(String deliveryMethodDesc) {
        this.deliveryMethodDesc = deliveryMethodDesc;
    }

    public String getDeliveryMethodId() {
        return deliveryMethodId;
    }

    public void setDeliveryMethodId(String deliveryMethodId) {
        this.deliveryMethodId = deliveryMethodId;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "coupon=" + coupon +
                ", deductionMoney=" + deductionMoney +
                ", deliveryMethodDesc='" + deliveryMethodDesc + '\'' +
                ", deliveryMethodId='" + deliveryMethodId + '\'' +
                ", freight=" + freight +
                ", money=" + money +
                ", nums=" + nums +
                ", appointmentsDate='" + appointmentsDate + '\'' +
                ", appointmentsTime='" + appointmentsTime + '\'' +
                ", orderId='" + orderId + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderStatusDesc='" + orderStatusDesc + '\'' +
                ", payType='" + payType + '\'' +
                ", payTypeDesc='" + payTypeDesc + '\'' +
                ", remark='" + remark + '\'' +
                ", orderTime=" + orderTime +
                ", payMoney=" + payMoney +
                ", payTime=" + payTime +
                ", price=" + price +
                ", totalPrice=" + totalPrice +
                ", goodsList=" + goodsList +
                ", setMealGoodsList=" + setMealGoodsList +
                ", orderRecipientInfo=" + orderRecipientInfo +
                ", hospital=" + hospital +
                ", store=" + store +
                '}';
    }
}
