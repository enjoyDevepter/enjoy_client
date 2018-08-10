package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

/**
 * Created by guomin on 2018/7/28.
 */

public class PayOrderResponse extends BaseResponse {

    private String orderId;
    private long orderTime;
    private long payMoney;
    private String payStatus;
    private List<Goods> goodsList;
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

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
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
        return "PayOrderResponse{" +
                "orderId='" + orderId + '\'' +
                ", orderTime=" + orderTime +
                ", payMoney=" + payMoney +
                ", payStatus='" + payStatus + '\'' +
                ", goodsList=" + goodsList +
                ", payEntryList=" + payEntryList +
                '}';
    }

    public static class Goods {
        private String goodsId;
        private String merchId;
        private String code;
        private String image;
        private double marketPrice;
        private double costPrice;
        private String name;
        private double salePrice;
        private String title;
        private int nums;
        private GoodsSpce goodsSpecValue;

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

        public GoodsSpce getGoodsSpecValue() {
            return goodsSpecValue;
        }

        public void setGoodsSpecValue(GoodsSpce goodsSpecValue) {
            this.goodsSpecValue = goodsSpecValue;
        }

        @Override
        public String toString() {
            return "Goods{" +
                    "goodsId='" + goodsId + '\'' +
                    ", merchId='" + merchId + '\'' +
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

    public static class GoodsSpce {
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
            return "GoodsSpce{" +
                    "specValueId='" + specValueId + '\'' +
                    ", specValueName='" + specValueName + '\'' +
                    '}';
        }
    }

    public static class PayEntry {
        private String payId;
        private String type;
        private String name;
        private String image;
        private String extendParams;

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
    }

}



