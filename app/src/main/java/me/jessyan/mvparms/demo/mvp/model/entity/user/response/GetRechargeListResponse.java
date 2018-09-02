package me.jessyan.mvparms.demo.mvp.model.entity.user.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.ChargeBean;

public class GetRechargeListResponse extends BaseResponse {
    private int nextPageIndex;
    private List<ChargeBean> chargeList;

    @Override
    public String toString() {
        return "GetRechargeListResponse{" +
                "nextPageIndex=" + nextPageIndex +
                ", chargeList=" + chargeList +
                '}';
    }

    public int getNextPageIndex() {
        return nextPageIndex;
    }

    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }

    public List<ChargeBean> getChargeList() {
        return chargeList;
    }

    public void setChargeList(List<ChargeBean> chargeList) {
        this.chargeList = chargeList;
    }
}
