package me.jessyan.mvparms.demo.mvp.model.entity.doctor;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

public class UnLikeDoctorCommentRequest extends BaseRequest {
    private final int cmd = 668;
    private String doctorId;
    private String token;
    private String commentId;

    @Override
    public String toString() {
        return "LikeDoctorCommentRequest{" +
                "cmd=" + cmd +
                ", doctorId='" + doctorId + '\'' +
                ", token='" + token + '\'' +
                ", commentId='" + commentId + '\'' +
                '}';
    }

    public int getCmd() {
        return cmd;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }
}
