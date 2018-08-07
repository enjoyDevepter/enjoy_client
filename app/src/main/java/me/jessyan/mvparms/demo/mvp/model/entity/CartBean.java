package me.jessyan.mvparms.demo.mvp.model.entity;

import java.util.List;

/**
 * Created by guomin on 2018/8/6.
 */

public class CartBean {
    private long deductionMoney;
    private long payPrice;
    private long totalNums;
    private long totalPrice;
    private List<CartItem> cartItems;


    public long getDeductionMoney() {
        return deductionMoney;
    }

    public void setDeductionMoney(long deductionMoney) {
        this.deductionMoney = deductionMoney;
    }

    public long getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(long payPrice) {
        this.payPrice = payPrice;
    }

    public long getTotalNums() {
        return totalNums;
    }

    public void setTotalNums(long totalNums) {
        this.totalNums = totalNums;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @Override
    public String toString() {
        return "CartBean{" +
                "deductionMoney=" + deductionMoney +
                ", payPrice=" + payPrice +
                ", totalNums=" + totalNums +
                ", totalPrice=" + totalPrice +
                ", cartItems=" + cartItems +
                '}';
    }

    public class CartItem {
        private List<GoodsBean> goodsList;
        private Promotion promotion;

        public List<GoodsBean> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(List<GoodsBean> goodsList) {
            this.goodsList = goodsList;
        }

        public Promotion getPromotion() {
            return promotion;
        }

        public void setPromotion(Promotion promotion) {
            this.promotion = promotion;
        }

        @Override
        public String toString() {
            return "CartItem{" +
                    "goodsList=" + goodsList +
                    ", promotion=" + promotion +
                    '}';
        }
    }

    public class GoodsBean {
        private int attention;
        private int cnt;
        private double costPrice;
        private String goodsId;
        private String image;
        private double marketPrice;
        private String merchId;
        private String name;
        private int nums;
        private double salePrice;
        private int sales;
        private String status;
        private String title;
        private boolean check;

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

        public String getMerchId() {
            return merchId;
        }

        public void setMerchId(String merchId) {
            this.merchId = merchId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getNums() {
            return nums;
        }

        public void setNums(int nums) {
            this.nums = nums;
        }

        public double getSalePrice() {
            return salePrice;
        }

        public void setSalePrice(double salePrice) {
            this.salePrice = salePrice;
        }

        public int getSales() {
            return sales;
        }

        public void setSales(int sales) {
            this.sales = sales;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isCheck() {
            return check;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }

        @Override
        public String toString() {
            return "GoodsBean{" +
                    "attention=" + attention +
                    ", cnt=" + cnt +
                    ", costPrice=" + costPrice +
                    ", goodsId='" + goodsId + '\'' +
                    ", image='" + image + '\'' +
                    ", marketPrice=" + marketPrice +
                    ", merchId='" + merchId + '\'' +
                    ", name='" + name + '\'' +
                    ", nums=" + nums +
                    ", salePrice=" + salePrice +
                    ", sales=" + sales +
                    ", status='" + status + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }
    }

    public class Promotion {
        private String tip;
        private String tag;
        private String promotionId;
        private String rule;
        private String title;
        private String type;
        private String typeDesc;

        public String getTip() {
            return tip;
        }

        public void setTip(String tip) {
            this.tip = tip;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getPromotionId() {
            return promotionId;
        }

        public void setPromotionId(String promotionId) {
            this.promotionId = promotionId;
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
                    "tip='" + tip + '\'' +
                    ", tag='" + tag + '\'' +
                    ", promotionId='" + promotionId + '\'' +
                    ", rule='" + rule + '\'' +
                    ", title='" + title + '\'' +
                    ", type='" + type + '\'' +
                    ", typeDesc='" + typeDesc + '\'' +
                    '}';
        }
    }
}
