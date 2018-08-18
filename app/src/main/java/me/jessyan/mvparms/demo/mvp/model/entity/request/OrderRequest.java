package me.jessyan.mvparms.demo.mvp.model.entity.request;

/**
 * Created by guomin on 2018/7/25.
 */

public class OrderRequest extends BaseRequest {
    private String orderStatus;
    private String token;
    private int pageIndex = 1;
    private int pageSize = 10;
    private int cmd = 550;

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
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

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    @Override
    public String toString() {
        return "OrderRequest{" +
                "orderStatus='" + orderStatus + '\'' +
                ", token='" + token + '\'' +
                ", pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                ", cmd=" + cmd +
                '}';
    }
}
