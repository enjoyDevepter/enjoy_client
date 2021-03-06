package me.jessyan.mvparms.demo.mvp.model.entity.doctor.request;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

public class DoctorCommentInfoRequest extends BaseRequest {
    private int cmd = 662;
    private String commentId;

    @Override
    public String toString() {
        return "DoctorCommentInfoRequest{" +
                "cmd=" + cmd +
                ", commentId='" + commentId + '\'' +
                '}';
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }
}
