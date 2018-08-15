package me.jessyan.mvparms.demo.mvp.model.entity.request;

import java.util.List;

/**
 * Created by guomin on 2018/7/25.
 */

public class HGoodsOrderConfirmInfoRequest extends BaseRequest {

    private int cmd = 523;
    private String province;
    private String city;
    private String county;
    private long money;
    private String couponId;
    private String token;
    private List<HGoods> goodsList;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<HGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<HGoods> goodsList) {
        this.goodsList = goodsList;
    }

    @Override
    public String toString() {
        return "HGoodsOrderConfirmInfoRequest{" +
                "cmd=" + cmd +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", county='" + county + '\'' +
                ", money=" + money +
                ", couponId='" + couponId + '\'' +
                ", token='" + token + '\'' +
                ", goodsList=" + goodsList +
                '}';
    }

    public static class HGoods {
        private String goodsId;
        private String merchId;
        private String advanceDepositId;
        private double deposit;
        private double tailMoney;
        private int nums;
        private String promotionId;
        private double salePrice;

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
            return "HGoods{" +
                    "goodsId='" + goodsId + '\'' +
                    ", merchId='" + merchId + '\'' +
                    ", advanceDepositId='" + advanceDepositId + '\'' +
                    ", deposit=" + deposit +
                    ", tailMoney=" + tailMoney +
                    ", nums=" + nums +
                    ", promotionId='" + promotionId + '\'' +
                    ", salePrice=" + salePrice +
                    '}';
        }
    }
}