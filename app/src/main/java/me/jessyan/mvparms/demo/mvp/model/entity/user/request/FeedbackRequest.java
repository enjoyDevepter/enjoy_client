package me.jessyan.mvparms.demo.mvp.model.entity.user.request;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

public class FeedbackRequest extends BaseRequest {
    private final int cmd = 910;
    private String content;
    private String token;

    @Override
    public String toString() {
        return "FeedbackRequest{" +
                "cmd=" + cmd +
                ", content='" + content + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    public int getCmd() {
        return cmd;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
