package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.Coupon;

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
    private List<GoodsBean> goodsList;

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

    public List<GoodsBean> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsBean> goodsList) {
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

    public class GoodsBean {
        private int attention;
        private double costPrice;
        private String goodsId;
        private String merchId;
        private String image;
        private double marketPrice;
        private String name;
        private int sales;
        private double salePrice;
        private String title;
        private int nums;
        private String promotionId;
        private GoodsSpecValueBean goodsSpecValue;

        public int getAttention() {
            return attention;
        }

        public void setAttention(int attention) {
            this.attention = attention;
        }

        public double getCostPrice() {
            return costPrice;
        }

        public void setCostPrice(double costPrice) {
            this.costPrice = costPrice;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public String getMerchId() {
            return merchId;
        }

        public void setMerchId(String merchId) {
            this.merchId = merchId;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public double getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(double marketPrice) {
            this.marketPrice = marketPrice;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSales() {
            return sales;
        }

        public void setSales(int sales) {
            this.sales = sales;
        }

        public double getSalePrice() {
            return salePrice;
        }

        public void setSalePrice(double salePrice) {
            this.salePrice = salePrice;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getNums() {
            return nums;
        }

        public void setNums(int nums) {
            this.nums = nums;
        }

        public String getPromotionId() {
            return promotionId;
        }

        public void setPromotionId(String promotionId) {
            this.promotionId = promotionId;
        }

        public GoodsSpecValueBean getGoodsSpecValue() {
            return goodsSpecValue;
        }

        public void setGoodsSpecValue(GoodsSpecValueBean goodsSpecValue) {
            this.goodsSpecValue = goodsSpecValue;
        }

        @Override
        public String toString() {
            return "GoodsBean{" +
                    "attention=" + attention +
                    ", costPrice=" + costPrice +
                    ", goodsId='" + goodsId + '\'' +
                    ", merchId='" + merchId + '\'' +
                    ", image='" + image + '\'' +
                    ", marketPrice=" + marketPrice +
                    ", name='" + name + '\'' +
                    ", sales=" + sales +
                    ", salePrice=" + salePrice +
                    ", title='" + title + '\'' +
                    ", nums=" + nums +
                    ", promotionId='" + promotionId + '\'' +
                    ", goodsSpecValue=" + goodsSpecValue +
                    '}';
        }
    }

    public class GoodsSpecValueBean {
        private String specValueId;
        private String specValueName;

        public String getSpecValueId() {
            return specValueId;
        }

        public void setSpecValueId(String specValueId) {
            this.specValueId = specValueId;
        }

        public String getSpecValueName() {
            return specValueName;
        }

        public void setSpecValueName(String specValueName) {
            this.specValueName = specValueName;
        }

        @Override
        public String toString() {
            return "GoodsSpecValueBean{" +
                    "specValueId='" + specValueId + '\'' +
                    ", specValueName='" + specValueName + '\'' +
                    '}';
        }
    }

}
