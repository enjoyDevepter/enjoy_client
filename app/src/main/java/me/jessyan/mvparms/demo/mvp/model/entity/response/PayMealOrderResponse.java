package me.jessyan.mvparms.demo.mvp.model.entity.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by guomin on 2018/7/28.
 */

public class PayMealOrderResponse extends BaseResponse {

    private String orderId;
    private long orderTime;
    private long payMoney;
    private String payStatus;
    private MealGoods setMealGoods;
    private List<PayEntry> payEntryList;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public long getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(long payMoney) {
        this.payMoney = payMoney;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public List<PayEntry> getPayEntryList() {
        return payEntryList;
    }

    public void setPayEntryList(List<PayEntry> payEntryList) {
        this.payEntryList = payEntryList;
    }

    public MealGoods getSetMealGoods() {
        return setMealGoods;
    }

    public void setSetMealGoods(MealGoods setMealGoods) {
        this.setMealGoods = setMealGoods;
    }

    @Override
    public String toString() {
        return "PayMealOrderResponse{" +
                "orderId='" + orderId + '\'' +
                ", orderTime=" + orderTime +
                ", payMoney=" + payMoney +
                ", payStatus='" + payStatus + '\'' +
                ", setMealGoods=" + setMealGoods +
                ", payEntryList=" + payEntryList +
                '}';
    }

    public static class PayEntry implements Parcelable {
        public static final Creator<PayEntry> CREATOR = new Creator<PayEntry>() {
            @Override
            public PayEntry createFromParcel(Parcel in) {
                return new PayEntry(in);
            }

            @Override
            public PayEntry[] newArray(int size) {
                return new PayEntry[size];
            }
        };
        private String payId;
        private String type;
        private String name;
        private String image;
        private String extendParams;

        public PayEntry() {

        }

        protected PayEntry(Parcel in) {
            payId = in.readString();
            type = in.readString();
            name = in.readString();
            image = in.readString();
            extendParams = in.readString();
        }

        public String getPayId() {
            return payId;
        }

        public void setPayId(String payId) {
            this.payId = payId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getExtendParams() {
            return extendParams;
        }

        public void setExtendParams(String extendParams) {
            this.extendParams = extendParams;
        }

        @Override
        public String toString() {
            return "PayEntry{" +
                    "payId='" + payId + '\'' +
                    ", type='" + type + '\'' +
                    ", name='" + name + '\'' +
                    ", image='" + image + '\'' +
                    ", extendParams='" + extendParams + '\'' +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(payId);
            dest.writeString(type);
            dest.writeString(name);
            dest.writeString(image);
            dest.writeString(extendParams);
        }
    }

    public class MealGoods {
        private String setMealId;
        private int nums;
        private double salesPrice;
        private double totalPrice;
        private String image;
        private String name;
        private String title;

        public String getSetMealId() {
            return setMealId;
        }

        public void setSetMealId(String setMealId) {
            this.setMealId = setMealId;
        }

        public int getNums() {
            return nums;
        }

        public void setNums(int nums) {
            this.nums = nums;
        }

        public double getSalesPrice() {
            return salesPrice;
        }

        public void setSalesPrice(double salesPrice) {
            this.salesPrice = salesPrice;
        }

        public double getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return "MealGoods{" +
                    "setMealId='" + setMealId + '\'' +
                    ", nums=" + nums +
                    ", salesPrice=" + salesPrice +
                    ", totalPrice=" + totalPrice +
                    ", image='" + image + '\'' +
                    ", name='" + name + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }
    }

}



