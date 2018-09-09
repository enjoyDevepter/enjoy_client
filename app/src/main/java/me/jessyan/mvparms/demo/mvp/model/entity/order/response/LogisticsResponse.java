package me.jessyan.mvparms.demo.mvp.model.entity.order.response;

import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;

/**
 * Created by guomin on 2018/7/28.
 */

public class LogisticsResponse extends BaseResponse {

    private String logisticsCode;
    private String deliveryDate;
    private String url;

    public String getLogisticsCode() {
        return logisticsCode;
    }

    public void setLogisticsCode(String logisticsCode) {
        this.logisticsCode = logisticsCode;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "LogisticsResponse{" +
                "logisticsCode='" + logisticsCode + '\'' +
                ", deliveryDate='" + deliveryDate + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
