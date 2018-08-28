package me.jessyan.mvparms.demo.mvp.model.entity.user.response;

import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.Share;

public class ShareResponse extends BaseResponse {
    private Share share;

    public Share getShare() {
        return share;
    }

    public void setShare(Share share) {
        this.share = share;
    }

    @Override
    public String toString() {
        return "ShareResponse{" +
                "share=" + share +
                '}';
    }
}
