package me.jessyan.mvparms.demo.mvp.model.entity.user.response;

import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;

public class QiandaoInfoResponse extends BaseResponse {
    private String isSign;  // 1:是0:否
    private long point;
    private String url;

    @Override
    public String toString() {
        return "QiandaoInfoResponse{" +
                "isSign='" + isSign + '\'' +
                ", point=" + point +
                ", url='" + url + '\'' +
                '}';
    }

    public String getIsSign() {
        return isSign;
    }

    public void setIsSign(String isSign) {
        this.isSign = isSign;
    }

    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
