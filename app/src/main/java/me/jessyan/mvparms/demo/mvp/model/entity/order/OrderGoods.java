package me.jessyan.mvparms.demo.mvp.model.entity.order;

import me.jessyan.mvparms.demo.mvp.model.entity.GoodsSpecValue;

/**
 * Created by guomin on 2018/8/19.
 */

public class OrderGoods {
    private String goodsId;
    private String merchId;
    private String image;
    private String name;
    private double salePrice;
    private double deposit;
    private double tailMoney;
    private String title;
    private int nums;
    private String type;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public GoodsSpecValue getGoodsSpecValue() {
        return goodsSpecValue;
    }

    public void setGoodsSpecValue(GoodsSpecValue goodsSpecValue) {
        this.goodsSpecValue = goodsSpecValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "OrderGoods{" +
                "goodsId='" + goodsId + '\'' +
                ", merchId='" + merchId + '\'' +
                ", image='" + image + '\'' +
                ", name='" + name + '\'' +
                ", salePrice=" + salePrice +
                ", deposit=" + deposit +
                ", tailMoney=" + tailMoney +
                ", title='" + title + '\'' +
                ", nums=" + nums +
                ", type='" + type + '\'' +
                ", goodsSpecValue=" + goodsSpecValue +
                '}';
    }
}
