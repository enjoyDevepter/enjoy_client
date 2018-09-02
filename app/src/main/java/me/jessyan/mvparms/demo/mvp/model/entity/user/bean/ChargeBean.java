package me.jessyan.mvparms.demo.mvp.model.entity.user.bean;

import java.io.Serializable;

public class ChargeBean implements Serializable {
    private String orderId;
    private String orderType;
    private String orderTypeDesc;
    private String type;
    private String typeDesc;
    private String createDate;
    private String status;
    private String statusDesc;
    private long money;

    @Override
    public String toString() {
        return "ChargeBean{" +
                "orderId='" + orderId + '\'' +
                ", orderType='" + orderType + '\'' +
                ", orderTypeDesc='" + orderTypeDesc + '\'' +
                ", type='" + type + '\'' +
                ", typeDesc='" + typeDesc + '\'' +
                ", createDate='" + createDate + '\'' +
                ", status='" + status + '\'' +
                ", statusDesc='" + statusDesc + '\'' +
                ", money=" + money +
                '}';
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderTypeDesc() {
        return orderTypeDesc;
    }

    public void setOrderTypeDesc(String orderTypeDesc) {
        this.orderTypeDesc = orderTypeDesc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }
}
