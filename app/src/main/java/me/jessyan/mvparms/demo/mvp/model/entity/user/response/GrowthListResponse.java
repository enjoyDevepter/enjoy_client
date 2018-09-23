package me.jessyan.mvparms.demo.mvp.model.entity.user.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.GrowthInfo;

public class GrowthListResponse extends BaseResponse {

    private List<GrowthInfo> growthList;
    private int nextPageIndex;


    public List<GrowthInfo> getGrowthList() {
        return growthList;
    }

    public void setGrowthList(List<GrowthInfo> growthList) {
        this.growthList = growthList;
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
                "growthList=" + growthList +
                ", nextPageIndex=" + nextPageIndex +
                '}';
    }
}
