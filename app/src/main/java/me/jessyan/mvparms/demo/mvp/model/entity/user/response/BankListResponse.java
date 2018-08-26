package me.jessyan.mvparms.demo.mvp.model.entity.user.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.BankBean;

public class BankListResponse extends BaseResponse {
    private List<BankBean> bankList;

    @Override
    public String toString() {
        return "BankListResponse{" +
                "bankList=" + bankList +
                '}';
    }

    public List<BankBean> getBankList() {
        return bankList;
    }

    public void setBankList(List<BankBean> bankList) {
        this.bankList = bankList;
    }
}
