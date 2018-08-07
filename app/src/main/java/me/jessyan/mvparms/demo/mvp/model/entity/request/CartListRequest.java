package me.jessyan.mvparms.demo.mvp.model.entity.request;

/**
 * Created by guomin on 2018/7/25.
 */

public class CartListRequest extends BaseRequest {
    private int cmd = 1001;
    private String token;

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

    @Override
    public String toString() {
        return "CartListRequest{" +
                "cmd=" + cmd +
                ", token='" + token + '\'' +
                '}';
    }
}