package me.jessyan.mvparms.demo.mvp.model.entity.pay.response;

import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;

/**
 * Created by guomin on 2018/7/28.
 */

public class PayResponse extends BaseResponse {
    private String params;

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "PayResponse{" +
                "params='" + params + '\'' +
                '}';
    }
}



