package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.Address;

/**
 * Created by guomin on 2018/7/28.
 */

public class AddressListResponse extends BaseResponse {

    private int nextPageIndex;
    private List<Address> memberAddressList;

    public int getNextPageIndex() {
        return nextPageIndex;
    }

    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }

    public List<Address> getMemberAddressList() {
        return memberAddressList;
    }

    public void setMemberAddressList(List<Address> memberAddressList) {
        this.memberAddressList = memberAddressList;
    }

    @Override
    public String toString() {
        return "AddressListResponse{" +
                "nextPageIndex=" + nextPageIndex +
                ", memberAddressList=" + memberAddressList +
                '}';
    }
}
