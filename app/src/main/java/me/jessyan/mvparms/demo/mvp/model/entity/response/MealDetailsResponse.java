package me.jessyan.mvparms.demo.mvp.model.entity.response;

import me.jessyan.mvparms.demo.mvp.model.entity.MealGoods;

/**
 * Created by guomin on 2018/7/28.
 */

public class MealDetailsResponse extends BaseResponse {

    private MealGoods setMealGoods;

    private String shareUrl;

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public MealGoods getSetMealGoods() {
        return setMealGoods;
    }

    public void setSetMealGoods(MealGoods setMealGoods) {
        this.setMealGoods = setMealGoods;
    }

    @Override
    public String toString() {
        return "MealDetailsResponse{" +
                "setMealGoods=" + setMealGoods +
                ", shareUrl='" + shareUrl + '\'' +
                '}';
    }
}
