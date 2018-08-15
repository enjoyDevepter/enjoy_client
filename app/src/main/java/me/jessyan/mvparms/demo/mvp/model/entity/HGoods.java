package me.jessyan.mvparms.demo.mvp.model.entity;

/**
 * Created by guomin on 2018/7/29.
 */

public class HGoods {

    private int attention;
    private int cnt;
    private String advanceDepositId;
    private double deposit;
    private double tailMoney;
    private double costPrice;
    private String goodsId;
    private String merchId;
    private String image;
    private double marketPrice;
    private String name;
    private int sales;
    private double salesPrice;
    private String title;
    private String isFavorite;
    private int favorite;
    private String mobileDetail;
    private String shareUrl;
    private GoodsSpecValue goodsSpecValue;

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

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
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

    public GoodsSpecValue getGoodsSpecValue() {
        return goodsSpecValue;
    }

    public void setGoodsSpecValue(GoodsSpecValue goodsSpecValue) {
        this.goodsSpecValue = goodsSpecValue;
    }

    @Override
    public String toString() {
        return "HGoods{" +
                "attention=" + attention +
                ", cnt=" + cnt +
                ", advanceDepositId='" + advanceDepositId + '\'' +
                ", deposit='" + deposit + '\'' +
                ", tailMoney=" + tailMoney +
                ", costPrice=" + costPrice +
                ", goodsId='" + goodsId + '\'' +
                ", merchId='" + merchId + '\'' +
                ", image='" + image + '\'' +
                ", marketPrice=" + marketPrice +
                ", name='" + name + '\'' +
                ", sales=" + sales +
                ", salesPrice=" + salesPrice +
                ", title='" + title + '\'' +
                ", isFavorite='" + isFavorite + '\'' +
                ", favorite=" + favorite +
                ", mobileDetail='" + mobileDetail + '\'' +
                ", shareUrl='" + shareUrl + '\'' +
                ", goodsSpecValue=" + goodsSpecValue +
                '}';
    }
}

