package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.HAppointments;
import me.jessyan.mvparms.demo.mvp.model.entity.Coupon;

/**
 * Created by guomin on 2018/7/28.
 */

public class HGoodsOrderConfirmInfoResponse extends BaseResponse {
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
    private List<GoodsBean> goodsList;
    private List<HAppointments> appointmentsDateList;

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

    public List<GoodsBean> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsBean> goodsList) {
        this.goodsList = goodsList;
    }

    public List<HAppointments> getAppointmentsDateList() {
        return appointmentsDateList;
    }

    public void setAppointmentsDateList(List<HAppointments> appointmentsDateList) {
        this.appointmentsDateList = appointmentsDateList;
    }

    @Override
    public String toString() {
        return "HGoodsOrderConfirmInfoResponse{" +
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
                ", goodsList=" + goodsList +
                ", appointmentsDateList=" + appointmentsDateList +
                '}';
    }

    public class GoodsBean {
        private int attention;
        private int cnt;
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
        private String advanceDepositId;
        private double deposit;
        private double tailMoney;

        public int getAttention() {
            return attention;
        }

        public void setAttention(int attention) {
            this.attention = attention;
        }

        public int getCnt() {
            return cnt;
        }

        public void setCnt(int cnt) {
            this.cnt = cnt;
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

        public String getAdvanceDepositId() {
            return advanceDepositId;
        }

        public void setAdvanceDepositId(String advanceDepositId) {
            this.advanceDepositId = advanceDepositId;
        }

        public double getDeposit() {
            return deposit;
        }

        public void setDeposit(double deposit) {
            this.deposit = deposit;
        }

        public double getTailMoney() {
            return tailMoney;
        }

        public void setTailMoney(double tailMoney) {
            this.tailMoney = tailMoney;
        }

        @Override
        public String toString() {
            return "GoodsBean{" +
                    "attention=" + attention +
                    ", cnt=" + cnt +
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
                    ", advanceDepositId='" + advanceDepositId + '\'' +
                    ", deposit=" + deposit +
                    ", tailMoney=" + tailMoney +
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
