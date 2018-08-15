package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.GoodsSpecValue;
import me.jessyan.mvparms.demo.mvp.model.entity.PayEntry;

/**
 * Created by guomin on 2018/7/28.
 */

public class HGoodsPayOrderResponse extends BaseResponse {

    private String orderId;
    private long orderTime;
    private long payMoney;
    private long totalPrice;
    private String payType;
    private String payTypeDesc;
    private String payStatus;
    private List<HGoods> goodsList;
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

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
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

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public List<HGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<HGoods> goodsList) {
        this.goodsList = goodsList;
    }

    public List<PayEntry> getPayEntryList() {
        return payEntryList;
    }

    public void setPayEntryList(List<PayEntry> payEntryList) {
        this.payEntryList = payEntryList;
    }

    @Override
    public String toString() {
        return "HGoodsPayOrderResponse{" +
                "orderId='" + orderId + '\'' +
                ", orderTime=" + orderTime +
                ", payMoney=" + payMoney +
                ", totalPrice=" + totalPrice +
                ", payType='" + payType + '\'' +
                ", payTypeDesc='" + payTypeDesc + '\'' +
                ", payStatus='" + payStatus + '\'' +
                ", goodsList=" + goodsList +
                ", payEntryList=" + payEntryList +
                '}';
    }

    public class HGoods {
        private String goodsId;
        private String merchId;
        private String advanceDepositId;
        private double deposit;
        private double tailMoney;
        private String code;
        private String image;
        private double marketPrice;
        private double costPrice;
        private String name;
        private double salePrice;
        private String title;
        private int nums;
        private GoodsSpecValue goodsSpecValue;


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

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
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

        public double getCostPrice() {
            return costPrice;
        }

        public void setCostPrice(double costPrice) {
            this.costPrice = costPrice;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public GoodsSpecValue getGoodsSpecValue() {
            return goodsSpecValue;
        }

        public void setGoodsSpecValue(GoodsSpecValue goodsSpecValue) {
            this.goodsSpecValue = goodsSpecValue;
        }

        @Override
        public String toString() {
            return "HGoods{" +
                    "goodsId='" + goodsId + '\'' +
                    ", merchId='" + merchId + '\'' +
                    ", advanceDepositId='" + advanceDepositId + '\'' +
                    ", deposit=" + deposit +
                    ", tailMoney=" + tailMoney +
                    ", code='" + code + '\'' +
                    ", image='" + image + '\'' +
                    ", marketPrice=" + marketPrice +
                    ", costPrice=" + costPrice +
                    ", name='" + name + '\'' +
                    ", salePrice=" + salePrice +
                    ", title='" + title + '\'' +
                    ", nums=" + nums +
                    ", goodsSpecValue=" + goodsSpecValue +
                    '}';
        }
    }

}



