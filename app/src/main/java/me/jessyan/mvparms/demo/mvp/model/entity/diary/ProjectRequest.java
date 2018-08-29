package me.jessyan.mvparms.demo.mvp.model.entity.diary;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

/**
 * Created by guomin on 2018/8/28.
 */

public class ProjectRequest extends BaseRequest {
    private int cmd = 576;
    private String orderId;
    private String token;
    private int pageIndex = 1;
    private int pageSize = 10;

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

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "ProjectRequest{" +
                "cmd=" + cmd +
                ", orderId='" + orderId + '\'' +
                ", token='" + token + '\'' +
                ", pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                '}';
    }
}
