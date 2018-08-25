package me.jessyan.mvparms.demo.mvp.model.entity.user.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.BalanceBean;

public class GetConsumeInfoPageResponse extends BaseResponse {
    private int nextPageIndex;
    private List<BalanceBean> balanceList;

    @Override
    public String toString() {
        return "GetConsumeInfoPageResponse{" +
                "nextPageIndex=" + nextPageIndex +
                ", balanceList=" + balanceList +
                '}';
    }

    public int getNextPageIndex() {
        return nextPageIndex;
    }

    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }

    public List<BalanceBean> getBalanceList() {
        return balanceList;
    }

    public void setBalanceList(List<BalanceBean> balanceList) {
        this.balanceList = balanceList;
    }
}
