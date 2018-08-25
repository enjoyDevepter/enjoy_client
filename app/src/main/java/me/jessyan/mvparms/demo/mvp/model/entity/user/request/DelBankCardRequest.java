package me.jessyan.mvparms.demo.mvp.model.entity.user.request;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

public class DelBankCardRequest extends BaseRequest {
    private final int cmd = 209;
    private String token;
    private String id;

    @Override
    public String toString() {
        return "DelBankCardRequest{" +
                "cmd=" + cmd +
                ", token='" + token + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public int getCmd() {
        return cmd;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
