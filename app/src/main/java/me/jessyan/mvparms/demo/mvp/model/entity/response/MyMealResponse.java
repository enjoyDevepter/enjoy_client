package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.order.Order;

/**
 * Created by guomin on 2018/7/28.
 */

public class MyMealResponse extends BaseResponse {

    private List<Order> orderList;
    private int nextPageIndex;

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public int getNextPageIndex() {
        return nextPageIndex;
    }

    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }

    @Override
    public String toString() {
        return "MyMealResponse{" +
                "orderList=" + orderList +
                ", nextPageIndex=" + nextPageIndex +
                '}';
    }
}
