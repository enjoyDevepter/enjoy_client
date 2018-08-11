package me.jessyan.mvparms.demo.mvp.model.entity;

import java.util.List;

/**
 * Created by guomin on 2018/7/29.
 */

public class MealGoods {

    private int favorite;
    private String isFavorite;
    private int cnt;
    private double totalPrice;
    private String setMealId;
    private String image;
    private List<String> images;
    private String name;
    private int sales;
    private double salesPrice;
    private String title;
    private List<Goods> goodsList;

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

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getSetMealId() {
        return setMealId;
    }

    public void setSetMealId(String setMealId) {
        this.setMealId = setMealId;
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

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "MealGoods{" +
                "favorite=" + favorite +
                ", isFavorite='" + isFavorite + '\'' +
                ", cnt=" + cnt +
                ", totalPrice=" + totalPrice +
                ", setMealId='" + setMealId + '\'' +
                ", image='" + image + '\'' +
                ", images=" + images +
                ", name='" + name + '\'' +
                ", sales=" + sales +
                ", salesPrice=" + salesPrice +
                ", title='" + title + '\'' +
                ", goodsList=" + goodsList +
                '}';
    }

    public static class Goods {
        private String goodsId;
        private String merchId;
        private String name;
        private String title;
        private String image;
        private int nums;
        private double salesPrice;
        private double marketPrice;
        private double costPrice;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getNums() {
            return nums;
        }

        public void setNums(int nums) {
            this.nums = nums;
        }

        public double getSalesPrice() {
            return salesPrice;
        }

        public void setSalesPrice(double salesPrice) {
            this.salesPrice = salesPrice;
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

        @Override
        public String toString() {
            return "Goods{" +
                    "goodsId='" + goodsId + '\'' +
                    ", merchId='" + merchId + '\'' +
                    ", name='" + name + '\'' +
                    ", title='" + title + '\'' +
                    ", image='" + image + '\'' +
                    ", nums=" + nums +
                    ", salesPrice=" + salesPrice +
                    ", marketPrice=" + marketPrice +
                    ", costPrice=" + costPrice +
                    '}';
        }
    }
}

