package me.jessyan.mvparms.demo.mvp.model.entity.pay.request;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

/**
 * Created by guomin on 2018/7/29.
 */

public class PayInfoRequest extends BaseRequest {
    private int cmd = 554;
    private String token;
    private String orderId;

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
        return "PayInfoRequest{" +
                "cmd=" + cmd +
                ", token='" + token + '\'' +
                ", orderId='" + orderId + '\'' +
                '}';
    }
}