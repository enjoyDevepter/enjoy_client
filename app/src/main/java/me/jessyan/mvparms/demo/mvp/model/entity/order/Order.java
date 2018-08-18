package me.jessyan.mvparms.demo.mvp.model.entity.order;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.GoodsSpecValue;

/**
 * Created by guomin on 2018/8/17.
 */

public class Order {

    private long orderTime;
    private int nums;
    private String orderId;
    private String orderStatus;
    private long payMoney;
    private long price;
    private long totalPrice;
    private List<Goods> goodsList;

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public long getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(long payMoney) {
        this.payMoney = payMoney;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }


    @Override
    public String toString() {
        return "Order{" +
                "orderTime=" + orderTime +
                ", nums=" + nums +
                ", orderId='" + orderId + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", payMoney=" + payMoney +
                ", price=" + price +
                ", totalPrice=" + totalPrice +
                ", goodsList=" + goodsList +
                '}';
    }

    public class Goods {

        private String goodsId;
        private String merchId;
        private String image;
        private String name;
        private double salesPrice;
        private String title;
        private int nums;
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
            return "Goods{" +
                    "goodsId='" + goodsId + '\'' +
                    ", merchId='" + merchId + '\'' +
                    ", image='" + image + '\'' +
                    ", name='" + name + '\'' +
                    ", salesPrice=" + salesPrice +
                    ", title='" + title + '\'' +
                    ", nums=" + nums +
                    ", goodsSpecValue=" + goodsSpecValue +
                    '}';
        }
    }
}
