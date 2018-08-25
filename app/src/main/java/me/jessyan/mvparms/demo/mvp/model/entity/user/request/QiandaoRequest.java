package me.jessyan.mvparms.demo.mvp.model.entity.user.request;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

public class QiandaoRequest extends BaseRequest {
    private final int cmd = 1200;
    private String token;

    @Override
    public String toString() {
        return "QiandaoRequest{" +
                "cmd=" + cmd +
                ", token='" + token + '\'' +
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
}
