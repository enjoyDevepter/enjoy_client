package me.jessyan.mvparms.demo.mvp.model.entity;

/**
 * Created by guomin on 2018/7/29.
 */

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
    private double salesPrice;
    private String title;
    private int nextPageIndex;

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
                ", salesPrice=" + salesPrice +
                ", title='" + title + '\'' +
                ", nextPageIndex=" + nextPageIndex +
                '}';
    }
}

