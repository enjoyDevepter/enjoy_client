package me.jessyan.mvparms.demo.mvp.model.entity.request;

import me.jessyan.mvparms.demo.mvp.model.entity.Address;

/**
 * Created by guomin on 2018/7/25.
 */

public class ModifyAddressRequest extends BaseRequest {
    private int cmd = 204;
    private String token;
    private Address memberAddress;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Address getMemberAddress() {
        return memberAddress;
    }

    public void setMemberAddress(Address memberAddress) {
        this.memberAddress = memberAddress;
    }

    @Override
    public String toString() {
        return "ModifyAddressRequest{" +
                "cmd=" + cmd +
                ", token='" + token + '\'' +
                ", memberAddress=" + memberAddress +
                '}';
    }
}