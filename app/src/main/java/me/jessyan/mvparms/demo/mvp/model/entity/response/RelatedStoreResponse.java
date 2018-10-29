package me.jessyan.mvparms.demo.mvp.model.entity.response;

import me.jessyan.mvparms.demo.mvp.model.entity.Store;

/**
 * Created by guomin on 2018/7/28.
 */

public class RelatedStoreResponse extends BaseResponse {

    private Store store;

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    @Override
    public String toString() {
        return "RelatedStoreResponse{" +
                "store=" + store +
                '}';
    }
}
