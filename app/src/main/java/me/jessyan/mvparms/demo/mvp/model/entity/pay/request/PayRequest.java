package me.jessyan.mvparms.demo.mvp.model.entity.pay.request;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

/**
 * Created by guomin on 2018/7/29.
 */

public class PayRequest extends BaseRequest {
    private int cmd = 1265;
    private String token;
    private String payId;
    private long amount;
    private String orderId;

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "PayRequest{" +
                "cmd=" + cmd +
                ", token='" + token + '\'' +
                ", payId='" + payId + '\'' +
                ", amount=" + amount +
                ", orderId='" + orderId + '\'' +
                '}';
    }
}