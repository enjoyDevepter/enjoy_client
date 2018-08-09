package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.GoodsDetails;

/**
 * Created by guomin on 2018/7/28.
 */

public class GoodsDetailsResponse extends BaseResponse {

    private String recom;
    private GoodsDetails.Goods goods;
    private List<GoodsDetails.GoodsSpec> goodsSpecList;
    private List<GoodsDetails.GoodsSpecValue> goodsSpecValueList;
    private List<String> images;
    private List<GoodsDetails.Promotion> promotionList;


    public GoodsDetails.Goods getGoods() {
        return goods;
    }

    public void setGoods(GoodsDetails.Goods goods) {
        this.goods = goods;
    }

    public List<GoodsDetails.GoodsSpec> getGoodsSpecList() {
        return goodsSpecList;
    }

    public void setGoodsSpecList(List<GoodsDetails.GoodsSpec> goodsSpecList) {
        this.goodsSpecList = goodsSpecList;
    }

    public List<GoodsDetails.GoodsSpecValue> getGoodsSpecValueList() {
        return goodsSpecValueList;
    }

    public void setGoodsSpecValueList(List<GoodsDetails.GoodsSpecValue> goodsSpecValueList) {
        this.goodsSpecValueList = goodsSpecValueList;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<GoodsDetails.Promotion> getPromotionList() {
        return promotionList;
    }

    public void setPromotionList(List<GoodsDetails.Promotion> promotionList) {
        this.promotionList = promotionList;
    }

    @Override
    public String toString() {
        return "GoodsDetailsResponse{" +
                "goods=" + goods +
                ", goodsSpecList=" + goodsSpecList +
                ", goodsSpecValueList=" + goodsSpecValueList +
                ", images=" + images +
                ", promotionList=" + promotionList +
                '}';
    }
}
