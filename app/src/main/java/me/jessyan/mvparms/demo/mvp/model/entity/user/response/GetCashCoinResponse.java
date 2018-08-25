package me.jessyan.mvparms.demo.mvp.model.entity.user.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.CashBean;

public class GetCashCoinResponse extends BaseResponse {
    private int nextPageIndex;
    private List<CashBean> cashList;

    @Override
    public String toString() {
        return "GetCashCoinResponse{" +
                "nextPageIndex=" + nextPageIndex +
                ", cashList=" + cashList +
                '}';
    }

    public int getNextPageIndex() {
        return nextPageIndex;
    }

    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }

    public List<CashBean> getCashList() {
        return cashList;
    }

    public void setCashList(List<CashBean> cashList) {
        this.cashList = cashList;
    }
}
