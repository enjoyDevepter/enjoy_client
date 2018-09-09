package me.jessyan.mvparms.demo.mvp.model.entity.user.response;

import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;

public class RechargeResponse extends BaseResponse {
    private String params;
    private String status;
    private String remind;
    private String money;
    private String orderId;


    @Override
    public String toString() {
        return "RechargeResponse{" +
                "params='" + params + '\'' +
                ", status='" + status + '\'' +
                ", remind='" + remind + '\'' +
                ", money='" + money + '\'' +
                ", orderId='" + orderId + '\'' +
                '}';
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemind() {
        return remind;
    }

    public void setRemind(String remind) {
        this.remind = remind;
    }
}
