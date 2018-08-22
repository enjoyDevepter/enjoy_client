package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.HGoods;

/**
 * Created by guomin on 2018/7/28.
 */

public class HGoodsListResponse extends BaseResponse {

    private List<HGoods> goodsList;
    private int nextPageIndex;

    public List<HGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<HGoods> goodsList) {
        this.goodsList = goodsList;
    }

    public int getNextPageIndex() {
        return nextPageIndex;
    }

    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }

    @Override
    public String toString() {
        return "HGoodsListResponse{" +
                "goodsList=" + goodsList +
                ", nextPageIndex=" + nextPageIndex +
                '}';
    }
}
