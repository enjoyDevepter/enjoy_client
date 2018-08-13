package me.jessyan.mvparms.demo.mvp.model.entity.request;

/**
 * Created by guomin on 2018/7/25.
 */

public class DiaryImagesRequest extends BaseRequest {
    private int cmd = 822;
    private int pageIndex = 1;
    private int pageSize = 10;
    private String memberId;
    private String goodsId;
    private String merchId;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
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

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getMerchId() {
        return merchId;
    }

    public void setMerchId(String merchId) {
        this.merchId = merchId;
    }

    @Override
    public String toString() {
        return "DiaryImagesRequest{" +
                "cmd=" + cmd +
                ", pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                ", memberId='" + memberId + '\'' +
                ", goodsId='" + goodsId + '\'' +
                ", merchId='" + merchId + '\'' +
                '}';
    }
}

