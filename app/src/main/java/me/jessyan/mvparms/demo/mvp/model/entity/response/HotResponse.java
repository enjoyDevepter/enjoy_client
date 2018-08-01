package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

/**
 * Created by guomin on 2018/7/25.
 */

public class HotResponse extends BaseResponse {

    private List<Key> goodsSearchKeywordList;

    public List<Key> getGoodsSearchKeywordList() {
        return goodsSearchKeywordList;
    }

    public void setGoodsSearchKeywordList(List<Key> goodsSearchKeywordList) {
        this.goodsSearchKeywordList = goodsSearchKeywordList;
    }

    @Override
    public String toString() {
        return "HotResponse{" +
                "goodsSearchKeywordList=" + goodsSearchKeywordList +
                '}';
    }

    public class Key {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Key{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
