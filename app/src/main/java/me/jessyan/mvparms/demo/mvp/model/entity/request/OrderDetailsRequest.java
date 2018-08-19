package me.jessyan.mvparms.demo.mvp.model.entity.request;

/**
 * Created by guomin on 2018/7/25.
 */

public class OrderDetailsRequest extends BaseRequest {
    private String orderId;
    private String token;
    private int cmd;

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

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    @Override
    public String toString() {
        return "OrderDetailsRequest{" +
                "orderId='" + orderId + '\'' +
                ", token='" + token + '\'' +
                ", cmd=" + cmd +
                '}';
    }
}
