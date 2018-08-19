package me.jessyan.mvparms.demo.mvp.model.entity.response;

import me.jessyan.mvparms.demo.mvp.model.entity.order.OrderDetails;

/**
 * Created by guomin on 2018/7/25.
 */

public class OrderDetailsResponse extends BaseResponse {

    private OrderDetails order;

    public OrderDetails getOrder() {
        return order;
    }

    public void setOrder(OrderDetails order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "OrderDetailsResponse{" +
                "order=" + order +
                '}';
    }
}
