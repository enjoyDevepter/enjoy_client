package me.jessyan.mvparms.demo.mvp.model.entity.request;

public class StoreInfoRequest {
    private int cmd = 705;
    private String storeId;
    private String token;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "StoreInfoRequest{" +
                "cmd=" + cmd +
                ", storeId='" + storeId + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
