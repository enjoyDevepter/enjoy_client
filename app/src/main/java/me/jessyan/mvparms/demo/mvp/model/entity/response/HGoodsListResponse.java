package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

/**
 * Created by guomin on 2018/7/28.
 */

public class HGoodsListResponse extends BaseResponse {

    private List<HGoods> goodsList;
    private int nextPageIndex;

    public List<HGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<HGoods> goodsList) {
        this.goodsList = goodsList;
    }

    public int getNextPageIndex() {
        return nextPageIndex;
    }

    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }

    @Override
    public String toString() {
        return "HGoodsListResponse{" +
                "goodsList=" + goodsList +
                ", nextPageIndex=" + nextPageIndex +
                '}';
    }

    public static class HGoods {
        private int attention;
        private String advanceDepositId;
        private double deposit;
        private double tailMoney;
        private int cnt;
        private double costPrice;
        private String goodsId;
        private String merchId;
        private String image;
        private double marketPrice;
        private String name;
        private int sales;
        private double salesPrice;
        private String title;
        private int nextPageIndex;
        private HGoodsSpec goodsSpecValue;

        public int getAttention() {
            return attention;
        }

        public void setAttention(int attention) {
            this.attention = attention;
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

        public double getSalesPrice() {
            return salesPrice;
        }

        public void setSalesPrice(double salesPrice) {
            this.salesPrice = salesPrice;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getNextPageIndex() {
            return nextPageIndex;
        }

        public void setNextPageIndex(int nextPageIndex) {
            this.nextPageIndex = nextPageIndex;
        }

        public HGoodsSpec getGoodsSpecValue() {
            return goodsSpecValue;
        }

        public void setGoodsSpecValue(HGoodsSpec goodsSpecValue) {
            this.goodsSpecValue = goodsSpecValue;
        }

        @Override
        public String toString() {
            return "HGoods{" +
                    "attention=" + attention +
                    ", advanceDepositId='" + advanceDepositId + '\'' +
                    ", deposit=" + deposit +
                    ", tailMoney=" + tailMoney +
                    ", cnt=" + cnt +
                    ", costPrice=" + costPrice +
                    ", goodsId='" + goodsId + '\'' +
                    ", merchId='" + merchId + '\'' +
                    ", image='" + image + '\'' +
                    ", marketPrice=" + marketPrice +
                    ", name='" + name + '\'' +
                    ", sales=" + sales +
                    ", salesPrice=" + salesPrice +
                    ", title='" + title + '\'' +
                    ", nextPageIndex=" + nextPageIndex +
                    ", goodsSpecValue=" + goodsSpecValue +
                    '}';
        }
    }

    public static class HGoodsSpec {
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
            return "HGoodsSpec{" +
                    "specValueId='" + specValueId + '\'' +
                    ", specValueName='" + specValueName + '\'' +
                    '}';
        }
    }

}
