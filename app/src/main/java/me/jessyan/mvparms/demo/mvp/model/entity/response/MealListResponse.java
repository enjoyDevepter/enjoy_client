package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.MealGoods;

/**
 * Created by guomin on 2018/7/28.
 */

public class MealListResponse extends BaseResponse {

    private List<MealGoods> setMealGoodsList;
    private int nextPageIndex;

    public List<MealGoods> getSetMealGoodsList() {
        return setMealGoodsList;
    }

    public void setSetMealGoodsList(List<MealGoods> setMealGoodsList) {
        this.setMealGoodsList = setMealGoodsList;
    }

    public int getNextPageIndex() {
        return nextPageIndex;
    }

    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }

    @Override
    public String toString() {
        return "MealListResponse{" +
                "setMealGoodsList=" + setMealGoodsList +
                ", nextPageIndex=" + nextPageIndex +
                '}';
    }
}
