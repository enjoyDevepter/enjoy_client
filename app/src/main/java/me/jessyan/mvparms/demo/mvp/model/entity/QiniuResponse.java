package me.jessyan.mvparms.demo.mvp.model.entity;

import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;

public class QiniuResponse extends BaseResponse {
    private String uploadToken;
    private String urlPrefix;

    @Override
    public String toString() {
        return "QiniuResponse{" +
                "uploadToken='" + uploadToken + '\'' +
                ", urlPrefix='" + urlPrefix + '\'' +
                '}';
    }

    public String getUploadToken() {
        return uploadToken;
    }

    public void setUploadToken(String uploadToken) {
        this.uploadToken = uploadToken;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }
}
