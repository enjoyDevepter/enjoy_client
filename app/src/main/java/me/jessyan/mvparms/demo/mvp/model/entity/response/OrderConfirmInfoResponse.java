package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.Coupon;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;

/**
 * Created by guomin on 2018/7/28.
 */

public class OrderConfirmInfoResponse extends BaseResponse {
    private long balance;
    private long deductionMoney;
    private long freight;
    private long money;
    private long price;
    private long totalPrice;
    private long payMoney;
    private long coupon;
    private String couponId;
    private List<Coupon> couponList;
    private Delivery deliveryMethod;
    private List<Goods> goodsList;

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getDeductionMoney() {
        return deductionMoney;
    }

    public void setDeductionMoney(long deductionMoney) {
        this.deductionMoney = deductionMoney;
    }

    public long getFreight() {
        return freight;
    }

    public void setFreight(long freight) {
        this.freight = freight;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public long getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(long payMoney) {
        this.payMoney = payMoney;
    }

    public long getCoupon() {
        return coupon;
    }

    public void setCoupon(long coupon) {
        this.coupon = coupon;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public List<Coupon> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<Coupon> couponList) {
        this.couponList = couponList;
    }

    public Delivery getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(Delivery deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    @Override
    public String toString() {
        return "OrderConfirmInfoResponse{" +
                "balance=" + balance +
                ", deductionMoney=" + deductionMoney +
                ", freight=" + freight +
                ", money=" + money +
                ", price=" + price +
                ", totalPrice=" + totalPrice +
                ", payMoney=" + payMoney +
                ", coupon=" + coupon +
                ", couponId='" + couponId + '\'' +
                ", couponList=" + couponList +
                ", deliveryMethod=" + deliveryMethod +
                ", goodsList=" + goodsList +
                '}';
    }


    public class Delivery {
        private String deliveryStaffId;
        private String deliveryStaffName;
        private String isDeliveryStaff;
        private String isStoreOneSelf;
        private String storeOneSelfId;
        private String storeOneSelfName;

        public String getDeliveryStaffId() {
            return deliveryStaffId;
        }

        public void setDeliveryStaffId(String deliveryStaffId) {
            this.deliveryStaffId = deliveryStaffId;
        }

        public String getDeliveryStaffName() {
            return deliveryStaffName;
        }

        public void setDeliveryStaffName(String deliveryStaffName) {
            this.deliveryStaffName = deliveryStaffName;
        }

        public String getIsDeliveryStaff() {
            return isDeliveryStaff;
        }

        public void setIsDeliveryStaff(String isDeliveryStaff) {
            this.isDeliveryStaff = isDeliveryStaff;
        }

        public String getIsStoreOneSelf() {
            return isStoreOneSelf;
        }

        public void setIsStoreOneSelf(String isStoreOneSelf) {
            this.isStoreOneSelf = isStoreOneSelf;
        }

        public String getStoreOneSelfId() {
            return storeOneSelfId;
        }

        public void setStoreOneSelfId(String storeOneSelfId) {
            this.storeOneSelfId = storeOneSelfId;
        }

        public String getStoreOneSelfName() {
            return storeOneSelfName;
        }

        public void setStoreOneSelfName(String storeOneSelfName) {
            this.storeOneSelfName = storeOneSelfName;
        }

        @Override
        public String toString() {
            return "Delivery{" +
                    "deliveryStaffId='" + deliveryStaffId + '\'' +
                    ", deliveryStaffName='" + deliveryStaffName + '\'' +
                    ", isDeliveryStaff='" + isDeliveryStaff + '\'' +
                    ", isStoreOneSelf='" + isStoreOneSelf + '\'' +
                    ", storeOneSelfId='" + storeOneSelfId + '\'' +
                    ", storeOneSelfName='" + storeOneSelfName + '\'' +
                    '}';
        }
    }


}
