package me.jessyan.mvparms.demo.mvp.model.entity;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

public class QiniuRequest extends BaseRequest {
    private final int cmd = 922;
    private String token;

    @Override
    public String toString() {
        return "QiniuRequest{" +
                "cmd=" + cmd +
                ", token='" + token + '\'' +
                '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCmd() {
        return cmd;
    }
}
