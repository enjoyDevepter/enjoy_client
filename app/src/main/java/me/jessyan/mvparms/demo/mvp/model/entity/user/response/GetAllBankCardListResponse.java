package me.jessyan.mvparms.demo.mvp.model.entity.user.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.BankCardBean;

public class GetAllBankCardListResponse extends BaseResponse {
    private int nextPageIndex;
    private List<BankCardBean> bankCardList;

    @Override
    public String toString() {
        return "GetAllBankCardListResponse{" +
                "nextPageIndex=" + nextPageIndex +
                ", bankCardList=" + bankCardList +
                '}';
    }

    public int getNextPageIndex() {
        return nextPageIndex;
    }

    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }

    public List<BankCardBean> getBankCardList() {
        return bankCardList;
    }

    public void setBankCardList(List<BankCardBean> bankCardList) {
        this.bankCardList = bankCardList;
    }
}
