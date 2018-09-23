package me.jessyan.mvparms.demo.mvp.model.entity.user.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;

public class MyDiaryInfoResponse extends BaseResponse {
    private String url;
    private int projectNum;
    private List<String> descList;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getProjectNum() {
        return projectNum;
    }

    public void setProjectNum(int projectNum) {
        this.projectNum = projectNum;
    }

    public List<String> getDescList() {
        return descList;
    }

    public void setDescList(List<String> descList) {
        this.descList = descList;
    }

    @Override
    public String toString() {
        return "MyDiaryInfoResponse{" +
                "url='" + url + '\'' +
                ", projectNum=" + projectNum +
                ", descList=" + descList +
                '}';
    }
}
