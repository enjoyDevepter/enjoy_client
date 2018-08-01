package me.jessyan.mvparms.demo.mvp.model.entity;

import java.util.List;

/**
 * Created by guomin on 2018/7/29.
 */

public class GoodsDetails {

    public class Goods {

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
        private String mobileDetail;
        private String shareUrl;
        private String goodsSpecValues;

        private List<GoodsSpec> goodsSpecList;
        private List<GoodsSpecValue> goodsSpecValueList;

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

        public String getMobileDetail() {
            return mobileDetail;
        }

        public void setMobileDetail(String mobileDetail) {
            this.mobileDetail = mobileDetail;
        }

        public String getShareUrl() {
            return shareUrl;
        }

        public void setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
        }

        public String getGoodsSpecValues() {
            return goodsSpecValues;
        }

        public void setGoodsSpecValues(String goodsSpecValues) {
            this.goodsSpecValues = goodsSpecValues;
        }

        public List<GoodsSpec> getGoodsSpecList() {
            return goodsSpecList;
        }

        public void setGoodsSpecList(List<GoodsSpec> goodsSpecList) {
            this.goodsSpecList = goodsSpecList;
        }

        public List<GoodsSpecValue> getGoodsSpecValueList() {
            return goodsSpecValueList;
        }

        public void setGoodsSpecValueList(List<GoodsSpecValue> goodsSpecValueList) {
            this.goodsSpecValueList = goodsSpecValueList;
        }

        @Override
        public String toString() {
            return "Goods{" +
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
                    ", mobileDetail='" + mobileDetail + '\'' +
                    ", shareUrl='" + shareUrl + '\'' +
                    ", goodsSpecValues='" + goodsSpecValues + '\'' +
                    ", goodsSpecList=" + goodsSpecList +
                    ", goodsSpecValueList=" + goodsSpecValueList +
                    '}';
        }
    }

    public class GoodsSpec {

        private String specId;

        private String specName;

        public String getSpecId() {
            return specId;
        }

        public void setSpecId(String specId) {
            this.specId = specId;
        }

        public String getSpecName() {
            return specName;
        }

        public void setSpecName(String specName) {
            this.specName = specName;
        }

        @Override
        public String toString() {
            return "GoodsSpec{" +
                    "specId='" + specId + '\'' +
                    ", specName='" + specName + '\'' +
                    '}';
        }
    }

    public class GoodsSpecValue {
        private String specId;
        private String specValueId;
        private String specValueName;

        public String getSpecId() {
            return specId;
        }

        public void setSpecId(String specId) {
            this.specId = specId;
        }

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
            return "GoodsSpecValue{" +
                    "specId='" + specId + '\'' +
                    ", specValueId='" + specValueId + '\'' +
                    ", specValueName='" + specValueName + '\'' +
                    '}';
        }
    }

    public class Promotion {
        private String id;
        private String rule;
        private String title;
        private String type;
        private String typeDesc;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRule() {
            return rule;
        }

        public void setRule(String rule) {
            this.rule = rule;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTypeDesc() {
            return typeDesc;
        }

        public void setTypeDesc(String typeDesc) {
            this.typeDesc = typeDesc;
        }

        @Override
        public String toString() {
            return "Promotion{" +
                    "id='" + id + '\'' +
                    ", rule='" + rule + '\'' +
                    ", title='" + title + '\'' +
                    ", type='" + type + '\'' +
                    ", typeDesc='" + typeDesc + '\'' +
                    '}';
        }
    }


}

