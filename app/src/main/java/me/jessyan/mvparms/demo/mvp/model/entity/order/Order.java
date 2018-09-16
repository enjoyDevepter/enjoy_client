package me.jessyan.mvparms.demo.mvp.model.entity.order;

import java.util.List;

/**
 * Created by guomin on 2018/8/17.
 */

public class Order {

    private long orderTime;
    private int nums;
    private String orderId;
    private String orderType;
    private String orderTypeDesc;
    private String orderStatus;
    private long payMoney;
    private long price;
    private long totalPrice;
    private String desc;
    private List<OrderGoods> goodsList;
    private List<MealGoods> setMealGoodsList;


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

    public List<OrderGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<OrderGoods> goodsList) {
        this.goodsList = goodsList;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderTypeDesc() {
        return orderTypeDesc;
    }

    public void setOrderTypeDesc(String orderTypeDesc) {
        this.orderTypeDesc = orderTypeDesc;
    }

    public List<MealGoods> getSetMealGoodsList() {
        return setMealGoodsList;
    }

    public void setSetMealGoodsList(List<MealGoods> setMealGoodsList) {
        this.setMealGoodsList = setMealGoodsList;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderTime=" + orderTime +
                ", nums=" + nums +
                ", orderId='" + orderId + '\'' +
                ", orderType='" + orderType + '\'' +
                ", orderTypeDesc='" + orderTypeDesc + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", payMoney=" + payMoney +
                ", price=" + price +
                ", totalPrice=" + totalPrice +
                ", desc='" + desc + '\'' +
                ", goodsList=" + goodsList +
                ", setMealGoodsList=" + setMealGoodsList +
                '}';
    }


    public class MealGoods {
        private String setMealId;
        private String image;
        private String name;
        private double salePrice;
        private double totalPrice;
        private String title;
        private int nums;

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

        public double getSalePrice() {
            return salePrice;
        }

        public void setSalePrice(double salePrice) {
            this.salePrice = salePrice;
        }

        public double getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
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

        @Override
        public String toString() {
            return "MealGoods{" +
                    "setMealId='" + setMealId + '\'' +
                    ", image='" + image + '\'' +
                    ", name='" + name + '\'' +
                    ", salePrice=" + salePrice +
                    ", totalPrice=" + totalPrice +
                    ", title='" + title + '\'' +
                    ", nums=" + nums +
                    '}';
        }
    }
}
