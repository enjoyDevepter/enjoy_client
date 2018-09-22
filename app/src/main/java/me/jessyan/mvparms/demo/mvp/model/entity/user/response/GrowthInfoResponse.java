package me.jessyan.mvparms.demo.mvp.model.entity.user.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.GrowthInfo;

public class GrowthInfoResponse extends BaseResponse {

    private String url;
    private long growth;
    private String desc;
    private List<GrowthInfo> growthInfoList;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getGrowth() {
        return growth;
    }

    public void setGrowth(long growth) {
        this.growth = growth;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<GrowthInfo> getGrowthInfoList() {
        return growthInfoList;
    }

    public void setGrowthInfoList(List<GrowthInfo> growthInfoList) {
        this.growthInfoList = growthInfoList;
    }

    @Override
    public String toString() {
        return "GrowthInfoResponse{" +
                "url='" + url + '\'' +
                ", growth=" + growth +
                ", desc='" + desc + '\'' +
                ", growthInfoList=" + growthInfoList +
                '}';
    }
}
