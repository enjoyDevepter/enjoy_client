package me.jessyan.mvparms.demo.mvp.model.entity.user.request;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

/**
 * Created by guomin on 2018/7/25.
 */

public class RelatedStoreRequest extends BaseRequest {
    private int cmd = 704;
    private String token;
    private String storeId;

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

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "RelatedStoreRequest{" +
                "cmd=" + cmd +
                ", token='" + token + '\'' +
                ", storeId='" + storeId + '\'' +
                '}';
    }
}
