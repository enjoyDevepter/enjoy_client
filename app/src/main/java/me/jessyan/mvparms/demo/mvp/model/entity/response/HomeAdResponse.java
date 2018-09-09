package me.jessyan.mvparms.demo.mvp.model.entity.response;

import me.jessyan.mvparms.demo.mvp.model.entity.HomeAd;

/**
 * Created by guomin on 2018/7/28.
 */

public class HomeAdResponse extends BaseResponse {

    private HomeAd ad;

    public HomeAd getAd() {
        return ad;
    }

    public void setAd(HomeAd ad) {
        this.ad = ad;
    }

    @Override
    public String toString() {
        return "HomeAdResponse{" +
                "ad=" + ad +
                '}';
    }
}
