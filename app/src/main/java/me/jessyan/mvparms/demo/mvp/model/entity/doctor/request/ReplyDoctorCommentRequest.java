package me.jessyan.mvparms.demo.mvp.model.entity.doctor.request;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

public class ReplyDoctorCommentRequest extends BaseRequest {
    private final int cmd = 664;
    private String commentId;
    private String content;
    private String token;

    @Override
    public String toString() {
        return "ReplyDoctorCommentRequest{" +
                "cmd=" + cmd +
                ", commentId='" + commentId + '\'' +
                ", content='" + content + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    public int getCmd() {
        return cmd;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
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
