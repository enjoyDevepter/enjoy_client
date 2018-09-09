package me.jessyan.mvparms.demo.mvp.model.entity.request;

/**
 * Created by guomin on 2018/7/25.
 */

public class HomeADRequest extends BaseRequest {
    private int cmd;
    private String token;
    private String adId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    @Override
    public String toString() {
        return "HomeADRequest{" +
                "cmd=" + cmd +
                ", token='" + token + '\'' +
                ", adId='" + adId + '\'' +
                '}';
    }
}
