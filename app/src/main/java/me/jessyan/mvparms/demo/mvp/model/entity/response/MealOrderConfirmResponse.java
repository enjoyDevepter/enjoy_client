package me.jessyan.mvparms.demo.mvp.model.entity.response;

import me.jessyan.mvparms.demo.mvp.model.entity.MealGoods;

/**
 * Created by guomin on 2018/7/28.
 */

public class MealOrderConfirmResponse extends BaseResponse {

    private long balance;
    private long money;
    private long price;
    private long totalPrice;
    private long payMoney;
    private MealGoods setMealGoods;

    public MealGoods getSetMealGoods() {
        return setMealGoods;
    }

    public void setSetMealGoods(MealGoods setMealGoods) {
        this.setMealGoods = setMealGoods;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
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

    public long getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(long payMoney) {
        this.payMoney = payMoney;
    }

    @Override
    public String toString() {
        return "MealOrderConfirmResponse{" +
                "balance=" + balance +
                ", money=" + money +
                ", price=" + price +
                ", totalPrice=" + totalPrice +
                ", payMoney=" + payMoney +
                ", setMealGoods=" + setMealGoods +
                '}';
    }
}
