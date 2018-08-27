package me.jessyan.mvparms.demo.mvp.model.entity.order.request;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

/**
 * Created by guomin on 2018/8/28.
 */

public class OrderOperationRequest extends BaseRequest {
    private int cmd = 553;
    private String orderId;
    private String token;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "OrderOperationRequest{" +
                "cmd=" + cmd +
                ", orderId='" + orderId + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
