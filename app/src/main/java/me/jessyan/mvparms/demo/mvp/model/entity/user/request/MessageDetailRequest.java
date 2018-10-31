package me.jessyan.mvparms.demo.mvp.model.entity.user.request;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

public class MessageDetailRequest extends BaseRequest {
    private int cmd;
    private String token;
    private String privateMessageId;
    private String noticeId;
    private String dynamicId;
    private String content;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getPrivateMessageId() {
        return privateMessageId;
    }

    public void setPrivateMessageId(String privateMessageId) {
        this.privateMessageId = privateMessageId;
    }

    public String getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(String dynamicId) {
        this.dynamicId = dynamicId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "MessageDetailRequest{" +
                "cmd=" + cmd +
                ", token='" + token + '\'' +
                ", privateMessageId='" + privateMessageId + '\'' +
                ", noticeId='" + noticeId + '\'' +
                ", dynamicId='" + dynamicId + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
