package me.jessyan.mvparms.demo.mvp.model.entity.user.request;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

public class BankListRequest extends BaseRequest {
    private final int cmd = 907;

    @Override
    public String toString() {
        return "BankListRequest{" +
                "cmd=" + cmd +
                '}';
    }

    public int getCmd() {
        return cmd;
    }
}
