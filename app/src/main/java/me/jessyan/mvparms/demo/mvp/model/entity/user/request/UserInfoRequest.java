package me.jessyan.mvparms.demo.mvp.model.entity.user.request;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

public class UserInfoRequest extends BaseRequest {
    private final int cmd = 104;
    private String token;

    @Override
    public String toString() {
        return "UserInfoRequest{" +
                "cmd=" + cmd +
                ", token='" + token + '\'' +
                '}';
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCmd() {
        return cmd;
    }

    public String getToken() {
        return token;
    }
}
