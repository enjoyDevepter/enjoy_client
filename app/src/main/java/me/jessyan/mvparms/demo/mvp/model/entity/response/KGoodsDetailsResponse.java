package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.HAppointments;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.model.entity.GoodsSpecValue;
import me.jessyan.mvparms.demo.mvp.model.entity.Promotion;

/**
 * Created by guomin on 2018/7/28.
 */

public class KGoodsDetailsResponse extends BaseResponse {
    private String recom;
    private Goods goods;
    private List<String> images;
    private List<Promotion> promotionList;
    private List<HAppointments> reservationDateList;
    private List<GoodsSpecValue> goodsSpecValueList;

    public String getRecom() {
        return recom;
    }

    public void setRecom(String recom) {
        this.recom = recom;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<Promotion> getPromotionList() {
        return promotionList;
    }

    public void setPromotionList(List<Promotion> promotionList) {
        this.promotionList = promotionList;
    }

    public List<HAppointments> getReservationDateList() {
        return reservationDateList;
    }

    public void setReservationDateList(List<HAppointments> reservationDateList) {
        this.reservationDateList = reservationDateList;
    }

    public List<GoodsSpecValue> getGoodsSpecValueList() {
        return goodsSpecValueList;
    }

    public void setGoodsSpecValueList(List<GoodsSpecValue> goodsSpecValueList) {
        this.goodsSpecValueList = goodsSpecValueList;
    }

    @Override
    public String toString() {
        return "KGoodsDetailsResponse{" +
                "recom='" + recom + '\'' +
                ", goods=" + goods +
                ", images=" + images +
                ", promotionList=" + promotionList +
                ", reservationDateList=" + reservationDateList +
                ", goodsSpecValueList=" + goodsSpecValueList +
                '}';
    }
}
