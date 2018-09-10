package me.jessyan.mvparms.demo.mvp.model.entity;

/**
 * Created by guomin on 2018/8/10.
 */
public class PickCoupon {
    private String couponPromotionId;
    private String couponTemplateId;
    private String name;
    private String startDate;
    private String endDate;
    private String intro;
    private double money;
    private String remark;
    private String status;
    private String statusDesc;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }
}