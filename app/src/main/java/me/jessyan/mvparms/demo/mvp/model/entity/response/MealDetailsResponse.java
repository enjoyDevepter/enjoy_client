package me.jessyan.mvparms.demo.mvp.model.entity.response;

import me.jessyan.mvparms.demo.mvp.model.entity.MealGoods;

/**
 * Created by guomin on 2018/7/28.
 */

public class MealDetailsResponse extends BaseResponse {

    private String tellphone;
    private MealGoods setMealGoods;

    public MealGoods getSetMealGoods() {
        return setMealGoods;
    }

    public void setSetMealGoods(MealGoods setMealGoods) {
        this.setMealGoods = setMealGoods;
    }

    public String getTellphone() {
        return tellphone;
    }

    public void setTellphone(String tellphone) {
        this.tellphone = tellphone;
    }

    @Override
    public String toString() {
        return "MealDetailsResponse{" +
                "tellphone='" + tellphone + '\'' +
                ", setMealGoods=" + setMealGoods +
                '}';
    }
}
