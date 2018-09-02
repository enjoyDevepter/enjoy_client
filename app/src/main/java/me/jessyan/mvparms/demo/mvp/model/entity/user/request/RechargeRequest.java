package me.jessyan.mvparms.demo.mvp.model.entity.user.request;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

/**充值*/
public class RechargeRequest extends BaseRequest {
    private final int cmd = 1262;
    private String payId;
    private long amount;
    private String orderId;
    private String type;  // 1:充值2:交易
    private String token;

    @Override
    public String toString() {
        return "RechargeRequest{" +
                "cmd=" + cmd +
                ", payId='" + payId + '\'' +
                ", amount=" + amount +
                ", orderId='" + orderId + '\'' +
                ", type='" + type + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    public int getCmd() {
        return cmd;
    }

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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
