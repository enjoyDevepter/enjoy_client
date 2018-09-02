package me.jessyan.mvparms.demo.mvp.model.entity.user.response;

import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;

public class RechargeResponse extends BaseResponse {
    private String params;

    @Override
    public String toString() {
        return "RechargeResponse{" +
                "params='" + params + '\'' +
                '}';
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
}
