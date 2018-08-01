package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.Category;

/**
 * Created by guomin on 2018/7/28.
 */

public class CategoryResponse extends BaseResponse {

    private List<Category> goodsCategoryList;

    public List<Category> getGoodsCategoryList() {
        return goodsCategoryList;
    }

    public void setGoodsCategoryList(List<Category> goodsCategoryList) {
        this.goodsCategoryList = goodsCategoryList;
    }

    @Override
    public String toString() {
        return "CategoryResponse{" +
                "goodsCategoryList=" + goodsCategoryList +
                '}';
    }
}
