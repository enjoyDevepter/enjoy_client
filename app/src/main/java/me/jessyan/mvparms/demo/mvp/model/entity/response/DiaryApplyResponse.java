package me.jessyan.mvparms.demo.mvp.model.entity.response;

/**
 * Created by guomin on 2018/7/28.
 */

public class DiaryApplyResponse extends BaseResponse {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "DiaryApplyResponse{" +
                "url='" + url + '\'' +
                '}';
    }
}
