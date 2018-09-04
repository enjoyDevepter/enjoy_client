package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.PayEntry;

/**
 * Created by guomin on 2018/7/28.
 */

public class PayMealOrderResponse extends BaseResponse {

    private String orderId;
    private long orderTime;
    private long payMoney;
    private String orderType;
    private String payType;
    private String payTypeDesc;
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

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayTypeDesc() {
        return payTypeDesc;
    }

    public void setPayTypeDesc(String payTypeDesc) {
        this.payTypeDesc = payTypeDesc;
    }

    @Override
    public String toString() {
        return "PayMealOrderResponse{" +
                ", orderType='" + orderType + '\'' +
                ", payType='" + payType + '\'' +
                ", payTypeDesc='" + payTypeDesc + '\'' +
                ", orderId='" + orderId + '\'' +
                ", orderTime=" + orderTime +
                ", payMoney=" + payMoney +
                ", payStatus='" + payStatus + '\'' +
                ", setMealGoods=" + setMealGoods +
                ", payEntryList=" + payEntryList +
                '}';
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



