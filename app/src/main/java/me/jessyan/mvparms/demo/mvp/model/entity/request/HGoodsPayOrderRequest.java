package me.jessyan.mvparms.demo.mvp.model.entity.request;

import java.util.List;

/**
 * Created by guomin on 2018/7/25.
 */

public class HGoodsPayOrderRequest extends BaseRequest {
    private int cmd = 524;
    private String memberAddressId;
    private String appointmentsDate;
    private String appointmentsTime;
    private String hospitalId;
    private String couponId;
    private long coupon;
    private long deductionMoney;
    private long money;
    private long freight;
    private long price;
    private long totalPrice;
    private long payMoney;
    private String remark;
    private String token;
    private List<OrderGoods> goodsList;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getMemberAddressId() {
        return memberAddressId;
    }

    public void setMemberAddressId(String memberAddressId) {
        this.memberAddressId = memberAddressId;
    }

    public String getAppointmentsDate() {
        return appointmentsDate;
    }

    public void setAppointmentsDate(String appointmentsDate) {
        this.appointmentsDate = appointmentsDate;
    }

    public String getAppointmentsTime() {
        return appointmentsTime;
    }

    public void setAppointmentsTime(String appointmentsTime) {
        this.appointmentsTime = appointmentsTime;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public long getCoupon() {
        return coupon;
    }

    public void setCoupon(long coupon) {
        this.coupon = coupon;
    }

    public long getDeductionMoney() {
        return deductionMoney;
    }

    public void setDeductionMoney(long deductionMoney) {
        this.deductionMoney = deductionMoney;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public long getFreight() {
        return freight;
    }

    public void setFreight(long freight) {
        this.freight = freight;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<OrderGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<OrderGoods> goodsList) {
        this.goodsList = goodsList;
    }

    @Override
    public String toString() {
        return "HGoodsPayOrderRequest{" +
                "cmd=" + cmd +
                ", memberAddressId='" + memberAddressId + '\'' +
                ", appointmentsDate='" + appointmentsDate + '\'' +
                ", appointmentsTime='" + appointmentsTime + '\'' +
                ", hospitalId='" + hospitalId + '\'' +
                ", couponId='" + couponId + '\'' +
                ", coupon=" + coupon +
                ", deductionMoney=" + deductionMoney +
                ", money=" + money +
                ", freight=" + freight +
                ", price=" + price +
                ", totalPrice=" + totalPrice +
                ", payMoney=" + payMoney +
                ", remark='" + remark + '\'' +
                ", token='" + token + '\'' +
                ", goodsList=" + goodsList +
                '}';
    }

    public static class OrderGoods {

        private String goodsId;
        private String advanceDepositId;
        private double deposit;
        private double tailMoney;
        private String merchId;
        private int nums;
        private String promotionId;
        private double salePrice;

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
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

        public String getMerchId() {
            return merchId;
        }

        public void setMerchId(String merchId) {
            this.merchId = merchId;
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

        public double getSalePrice() {
            return salePrice;
        }

        public void setSalePrice(double salePrice) {
            this.salePrice = salePrice;
        }

        @Override
        public String toString() {
            return "OrderGoods{" +
                    "goodsId='" + goodsId + '\'' +
                    ", advanceDepositId='" + advanceDepositId + '\'' +
                    ", deposit=" + deposit +
                    ", tailMoney=" + tailMoney +
                    ", merchId='" + merchId + '\'' +
                    ", nums=" + nums +
                    ", promotionId='" + promotionId + '\'' +
                    ", salePrice=" + salePrice +
                    '}';
        }
    }


}