package me.jessyan.mvparms.demo.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by guomin on 2018/8/10.
 */
public class Coupon implements Parcelable {
    public static final Creator<Coupon> CREATOR = new Creator<Coupon>() {
        @Override
        public Coupon createFromParcel(Parcel in) {
            return new Coupon(in);
        }

        @Override
        public Coupon[] newArray(int size) {
            return new Coupon[size];
        }
    };
    private String couponId;
    private String name;
    private long startDate;
    private long endDate;
    private String intro;
    private long money;
    private String remark;
    private String status;
    private String statusDesc;
    private String couponTemplateId;
    private String couponPromotionId;

    public Coupon() {
    }

    protected Coupon(Parcel in) {
        couponId = in.readString();
        name = in.readString();
        startDate = in.readLong();
        endDate = in.readLong();
        intro = in.readString();
        money = in.readLong();
        remark = in.readString();
        status = in.readString();
        statusDesc = in.readString();
        couponTemplateId = in.readString();
        couponPromotionId = in.readString();
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
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

    public String getCouponTemplateId() {
        return couponTemplateId;
    }

    public void setCouponTemplateId(String couponTemplateId) {
        this.couponTemplateId = couponTemplateId;
    }

    public String getCouponPromotionId() {
        return couponPromotionId;
    }

    public void setCouponPromotionId(String couponPromotionId) {
        this.couponPromotionId = couponPromotionId;
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "couponId='" + couponId + '\'' +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", intro='" + intro + '\'' +
                ", money=" + money +
                ", remark='" + remark + '\'' +
                ", status='" + status + '\'' +
                ", statusDesc='" + statusDesc + '\'' +
                ", couponTemplateId='" + couponTemplateId + '\'' +
                ", couponPromotionId='" + couponPromotionId + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(couponId);
        dest.writeString(name);
        dest.writeLong(startDate);
        dest.writeLong(endDate);
        dest.writeString(intro);
        dest.writeLong(money);
        dest.writeString(remark);
        dest.writeString(status);
        dest.writeString(statusDesc);
        dest.writeString(couponTemplateId);
        dest.writeString(couponPromotionId);
    }
}