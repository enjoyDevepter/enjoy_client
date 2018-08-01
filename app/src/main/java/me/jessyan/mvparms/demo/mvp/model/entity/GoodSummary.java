package me.jessyan.mvparms.demo.mvp.model.entity;

/**
 * Created by guomin on 2018/7/29.
 */

public class GoodSummary {

    private String name;
    private String image;
    private String redirectType;
    private String goodsId;
    private String salePrice;
    private String marketPrice;

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

    public String getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(String redirectType) {
        this.redirectType = redirectType;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    @Override
    public String toString() {
        return "GoodSummary{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", redirectType='" + redirectType + '\'' +
                ", goodsId='" + goodsId + '\'' +
                ", salePrice='" + salePrice + '\'' +
                ", marketPrice='" + marketPrice + '\'' +
                '}';
    }
}
