package me.jessyan.mvparms.demo.mvp.model.entity.user.bean;

import java.io.Serializable;

public class PrivateMessage implements Serializable {
    private String privateMessageId;
    private String content;
    private String sendTime;
    private String replyTime;
    private String replyContent;
    private String status;
    private String title;
    private String headImage;

    public String getPrivateMessageId() {
        return privateMessageId;
    }

    public void setPrivateMessageId(String privateMessageId) {
        this.privateMessageId = privateMessageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    @Override
    public String toString() {
        return "PrivateMessage{" +
                "privateMessageId='" + privateMessageId + '\'' +
                ", content='" + content + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", status='" + status + '\'' +
                ", title='" + title + '\'' +
                ", headImage='" + headImage + '\'' +
                '}';
    }
}
