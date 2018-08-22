package me.jessyan.mvparms.demo.mvp.model.entity.score;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;

public class UserScorePageResponse extends BaseResponse {
    private List<ScorePointBean> pointList;
    private int nextPageIndex;

    @Override
    public String toString() {
        return "UserScorePageResponse{" +
                "pointList=" + pointList +
                ", nextPageIndex=" + nextPageIndex +
                '}';
    }

    public List<ScorePointBean> getPointList() {
        return pointList;
    }

    public void setPointList(List<ScorePointBean> pointList) {
        this.pointList = pointList;
    }

    public int getNextPageIndex() {
        return nextPageIndex;
    }

    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }
}
