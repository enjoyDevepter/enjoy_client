package me.jessyan.mvparms.demo.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by guomin on 2018/7/29.
 */

public class Goods implements Parcelable {

    public static final Creator<Goods> CREATOR = new Creator<Goods>() {
        @Override
        public Goods createFromParcel(Parcel in) {
            return new Goods(in);
        }

        @Override
        public Goods[] newArray(int size) {
            return new Goods[size];
        }
    };
    private int attention;
    private int cnt;
    private double costPrice;
    private int favorite;
    private String goodsId;
    private String merchId;
    private String image;
    private String code;
    private double marketPrice;
    private String name;
    private String isFavorite;
    private String isRecom;
    private int sales;
    private double salePrice;
    private String title;
    private String promotionId;
    private long sysDate;
    private long startDate;
    private long endDate;
    private String type;
    private String mobileDetail;
    private String shareUrl;
    private double secKillPrice;
    private int nums;
    private GoodsSpecValue goodsSpecValue;
    private String status;
    private double vipPrice;
    private String advanceDepositId;
    private double deposit;
    private double tailMoney;
    private String orderId;


    public Goods() {
    }

    protected Goods(Parcel in) {
        attention = in.readInt();
        cnt = in.readInt();
        costPrice = in.readDouble();
        favorite = in.readInt();
        goodsId = in.readString();
        merchId = in.readString();
        image = in.readString();
        code = in.readString();
        marketPrice = in.readDouble();
        name = in.readString();
        isFavorite = in.readString();
        isRecom = in.readString();
        sales = in.readInt();
        salePrice = in.readDouble();
        title = in.readString();
        promotionId = in.readString();
        sysDate = in.readLong();
        startDate = in.readLong();
        endDate = in.readLong();
        type = in.readString();
        mobileDetail = in.readString();
        shareUrl = in.readString();
        secKillPrice = in.readDouble();
        nums = in.readInt();
        status = in.readString();
        vipPrice = in.readDouble();
        advanceDepositId = in.readString();
        deposit = in.readDouble();
        tailMoney = in.readDouble();
        orderId = in.readString();
    }

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

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getIsRecom() {
        return isRecom;
    }

    public void setIsRecom(String isRecom) {
        this.isRecom = isRecom;
    }

    public double getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(double vipPrice) {
        this.vipPrice = vipPrice;
    }

    public long getSysDate() {
        return sysDate;
    }

    public void setSysDate(long sysDate) {
        this.sysDate = sysDate;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public double getSecKillPrice() {
        return secKillPrice;
    }

    public void setSecKillPrice(double secKillPrice) {
        this.secKillPrice = secKillPrice;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdvanceDepositId() {
        return advanceDepositId;
    }

    public void setAdvanceDepositId(String advanceDepositId) {
        this.advanceDepositId = advanceDepositId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    @Override
    public String toString() {
        return "Goods{" +
                "attention=" + attention +
                ", cnt=" + cnt +
                ", costPrice=" + costPrice +
                ", favorite=" + favorite +
                ", goodsId='" + goodsId + '\'' +
                ", merchId='" + merchId + '\'' +
                ", image='" + image + '\'' +
                ", code='" + code + '\'' +
                ", marketPrice=" + marketPrice +
                ", name='" + name + '\'' +
                ", isFavorite='" + isFavorite + '\'' +
                ", isRecom='" + isRecom + '\'' +
                ", sales=" + sales +
                ", salePrice=" + salePrice +
                ", title='" + title + '\'' +
                ", promotionId='" + promotionId + '\'' +
                ", sysDate=" + sysDate +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", type='" + type + '\'' +
                ", mobileDetail='" + mobileDetail + '\'' +
                ", shareUrl='" + shareUrl + '\'' +
                ", secKillPrice=" + secKillPrice +
                ", nums=" + nums +
                ", goodsSpecValue=" + goodsSpecValue +
                ", status='" + status + '\'' +
                ", vipPrice=" + vipPrice +
                ", advanceDepositId='" + advanceDepositId + '\'' +
                ", deposit=" + deposit +
                ", tailMoney=" + tailMoney +
                ", orderId='" + orderId + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(attention);
        dest.writeInt(cnt);
        dest.writeDouble(costPrice);
        dest.writeInt(favorite);
        dest.writeString(goodsId);
        dest.writeString(merchId);
        dest.writeString(image);
        dest.writeString(code);
        dest.writeDouble(marketPrice);
        dest.writeString(name);
        dest.writeString(isFavorite);
        dest.writeString(isRecom);
        dest.writeInt(sales);
        dest.writeDouble(salePrice);
        dest.writeString(title);
        dest.writeString(promotionId);
        dest.writeLong(sysDate);
        dest.writeLong(startDate);
        dest.writeLong(endDate);
        dest.writeString(type);
        dest.writeString(mobileDetail);
        dest.writeString(shareUrl);
        dest.writeDouble(secKillPrice);
        dest.writeInt(nums);
        dest.writeString(status);
        dest.writeDouble(vipPrice);
        dest.writeString(advanceDepositId);
        dest.writeDouble(deposit);
        dest.writeDouble(tailMoney);
        dest.writeString(orderId);
    }
}

