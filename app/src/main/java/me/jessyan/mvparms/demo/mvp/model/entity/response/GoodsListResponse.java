package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.Ad;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;

/**
 * Created by guomin on 2018/7/28.
 */

public class GoodsListResponse extends BaseResponse {

    private List<Goods> goodsList;
    private List<Ad> carouselList;
    private int nextPageIndex;

    public List<Ad> getCarouselList() {
        return carouselList;
    }

    public void setCarouselList(List<Ad> carouselList) {
        this.carouselList = carouselList;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
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
        return "GoodsListResponse{" +
                "goodsList=" + goodsList +
                ", carouselList=" + carouselList +
                ", nextPageIndex=" + nextPageIndex +
                '}';
    }
}
