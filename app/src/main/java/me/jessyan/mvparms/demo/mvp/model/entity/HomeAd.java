package me.jessyan.mvparms.demo.mvp.model.entity;

/**
 * Created by guomin on 2018/7/29.
 */
public class HomeAd {

    private String adId;
    private String imageUrl;
    private String h5Url;
    private String redirectType;

    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(String redirectType) {
        this.redirectType = redirectType;
    }

    @Override
    public String toString() {
        return "HomeAd{" +
                "adId='" + adId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", h5Url='" + h5Url + '\'' +
                ", redirectType='" + redirectType + '\'' +
                '}';
    }
}