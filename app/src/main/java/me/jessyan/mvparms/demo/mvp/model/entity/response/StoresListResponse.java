package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.request.Store;

/**
 * Created by guomin on 2018/7/28.
 */

public class StoresListResponse extends BaseResponse {

    private List<Store> storeList;
    private int nextPageIndex;

    public List<Store> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<Store> storeList) {
        this.storeList = storeList;
    }

    public int getNextPageIndex() {
        return nextPageIndex;
    }

    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }

    @Override
    public String toString() {
        return "StoresListResponse{" +
                "storeList=" + storeList +
                ", nextPageIndex=" + nextPageIndex +
                '}';
    }
}
