package me.jessyan.mvparms.demo.mvp.model.entity.request;

/**
 * Created by guomin on 2018/7/25.
 */

public class PickCouponRequest extends BaseRequest {
    private int cmd;
    private String token;
    private String couponPromotionId;
    private String couponTemplateId;

    public String getCouponPromotionId() {
        return couponPromotionId;
    }

    public void setCouponPromotionId(String couponPromotionId) {
        this.couponPromotionId = couponPromotionId;
    }

    public String getCouponTemplateId() {
        return couponTemplateId;
    }

    public void setCouponTemplateId(String couponTemplateId) {
        this.couponTemplateId = couponTemplateId;
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
        return "PickCouponRequest{" +
                "cmd=" + cmd +
                ", token='" + token + '\'' +
                ", couponPromotionId='" + couponPromotionId + '\'' +
                ", couponTemplateId='" + couponTemplateId + '\'' +
                '}';
    }
}
