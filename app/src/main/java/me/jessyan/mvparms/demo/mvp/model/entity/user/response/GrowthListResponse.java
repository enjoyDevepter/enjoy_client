package me.jessyan.mvparms.demo.mvp.model.entity.user.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.GrowthInfo;

public class GrowthListResponse extends BaseResponse {

    private List<GrowthInfo> growthInfoList;
    private int nextPageIndex;

    public List<GrowthInfo> getGrowthInfoList() {
        return growthInfoList;
    }

    public void setGrowthInfoList(List<GrowthInfo> growthInfoList) {
        this.growthInfoList = growthInfoList;
    }

    public int getNextPageIndex() {
        return nextPageIndex;
    }

    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }

    @Override
    public String toString() {
        return "GrowthListResponse{" +
                "growthInfoList=" + growthInfoList +
                ", nextPageIndex=" + nextPageIndex +
                '}';
    }
}
