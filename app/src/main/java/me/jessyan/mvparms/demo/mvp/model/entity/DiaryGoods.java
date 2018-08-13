package me.jessyan.mvparms.demo.mvp.model.entity;


/**
 * 日志
 */
public class DiaryGoods {
    private double costPrice;
    private String goodsId;
    private String merchId;
    private String image;
    private double marketPrice;
    private String name;
    private double salePrice;
    private String title;

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

    @Override
    public String toString() {
        return "Goods{" +
                "costPrice=" + costPrice +
                ", goodsId='" + goodsId + '\'' +
                ", merchId='" + merchId + '\'' +
                ", image='" + image + '\'' +
                ", marketPrice=" + marketPrice +
                ", name='" + name + '\'' +
                ", salePrice=" + salePrice +
                ", title='" + title + '\'' +
                '}';
    }

}
