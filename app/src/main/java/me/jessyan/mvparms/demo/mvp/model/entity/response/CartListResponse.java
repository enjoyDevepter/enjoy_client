package me.jessyan.mvparms.demo.mvp.model.entity.response;

import me.jessyan.mvparms.demo.mvp.model.entity.CartBean;

/**
 * Created by guomin on 2018/7/28.
 */

public class CartListResponse extends BaseResponse {

    private CartBean cart;

    public CartBean getCart() {
        return cart;
    }

    public void setCart(CartBean cart) {
        this.cart = cart;
    }

    @Override
    public String toString() {
        return "CartListResponse{" +
                "cart=" + cart +
                '}';
    }
}
