package me.jessyan.mvparms.demo.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by guomin on 2018/7/29.
 */

public class PayGoods implements Parcelable {

    public static final Creator<PayGoods> CREATOR = new Creator<PayGoods>() {
        @Override
        public PayGoods createFromParcel(Parcel in) {
            return new PayGoods(in);
        }

        @Override
        public PayGoods[] newArray(int size) {
            return new PayGoods[size];
        }
    };
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
    private GoodsSpecValue goodsSpecValue;

    public PayGoods() {

    }

    protected PayGoods(Parcel in) {
        goodsId = in.readString();
        merchId = in.readString();
        code = in.readString();
        image = in.readString();
        marketPrice = in.readDouble();
        costPrice = in.readDouble();
        name = in.readString();
        salePrice = in.readDouble();
        title = in.readString();
        nums = in.readInt();
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
        return "PayGoods{" +
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(goodsId);
        dest.writeString(merchId);
        dest.writeString(code);
        dest.writeString(image);
        dest.writeDouble(marketPrice);
        dest.writeDouble(costPrice);
        dest.writeString(name);
        dest.writeDouble(salePrice);
        dest.writeString(title);
        dest.writeInt(nums);
    }
}

