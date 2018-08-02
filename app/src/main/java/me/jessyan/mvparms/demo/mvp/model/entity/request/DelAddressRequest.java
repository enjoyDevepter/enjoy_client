package me.jessyan.mvparms.demo.mvp.model.entity.request;

/**
 * Created by guomin on 2018/7/25.
 */

public class DelAddressRequest extends BaseRequest {

    private int cmd = 206;
    private String token;
    private String id;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DelAddressRequest{" +
                "cmd=" + cmd +
                ", token='" + token + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}