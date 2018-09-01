package me.jessyan.mvparms.demo.mvp.model.entity.user.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.DrawCashBean;

public class GetCashListResponse extends BaseResponse {
    private int nextPageIndex;
    private List<DrawCashBean> drawCashList;

    @Override
    public String toString() {
        return "GetCashListResponse{" +
                "nextPageIndex=" + nextPageIndex +
                ", drawCashList=" + drawCashList +
                '}';
    }

    public int getNextPageIndex() {
        return nextPageIndex;
    }

    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }

    public List<DrawCashBean> getDrawCashList() {
        return drawCashList;
    }

    public void setDrawCashList(List<DrawCashBean> drawCashList) {
        this.drawCashList = drawCashList;
    }
}
