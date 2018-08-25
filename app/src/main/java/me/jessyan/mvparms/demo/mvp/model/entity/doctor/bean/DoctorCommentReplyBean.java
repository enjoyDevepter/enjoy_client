package me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean;

import java.io.Serializable;

public class DoctorCommentReplyBean implements Serializable {
    private String commentId;
    private String commentReplayId;  // 回复id
    private String doctorId;
    private String content;
    private long createDate;
    private CommentMemberBean member;

    @Override
    public String toString() {
        return "DoctorCommentReplyBean{" +
                "commentId='" + commentId + '\'' +
                ", commentReplayId='" + commentReplayId + '\'' +
                ", doctorId='" + doctorId + '\'' +
                ", content='" + content + '\'' +
                ", createDate=" + createDate +
                ", member=" + member +
                '}';
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentReplayId() {
        return commentReplayId;
    }

    public void setCommentReplayId(String commentReplayId) {
        this.commentReplayId = commentReplayId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public CommentMemberBean getMember() {
        return member;
    }

    public void setMember(CommentMemberBean member) {
        this.member = member;
    }
}
