package me.jessyan.mvparms.demo.mvp.model.entity.score;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

public class UserScorePageRequest extends BaseRequest {
    private final int cmd = 1201;
    private String token;
    private int pageIndex = 1;
    private int pageSize = 10;

    @Override
    public String toString() {
        return "UserScorePageRequest{" +
                "cmd=" + cmd +
                ", token='" + token + '\'' +
                ", pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
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

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
