package me.jessyan.mvparms.demo.mvp.model.entity;


import java.util.List;

/**
 * 日志
 */
public class DiaryBean {
    private String content;
    private String goodsId;
    private String merchId;
    private String orderId;
    private String title;
    private List<String> imageList;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    @Override
    public String toString() {
        return "DiaryBean{" +
                "content='" + content + '\'' +
                ", goodsId='" + goodsId + '\'' +
                ", merchId='" + merchId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", title='" + title + '\'' +
                ", imageList=" + imageList +
                '}';
    }
}
