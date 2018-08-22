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
        private List<Goods> goodsList;
        private Promotion promotion;

        public List<Goods> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(List<Goods> goodsList) {
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
