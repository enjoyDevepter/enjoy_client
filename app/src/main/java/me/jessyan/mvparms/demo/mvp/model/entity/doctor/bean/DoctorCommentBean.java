package me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean;

import java.io.Serializable;

public class DoctorCommentBean implements Serializable {
    private int comment;  // 评论数
    private String commentId;  // 评论编号
    private String content;
    private long createDate;
    private String doctorId;
    private String isPraise;  //是否点赞 1是0否
    private int praise;  // 点赞数
    private String projectId;
    private int star;
    private int views;  // 阅读数
    private CommentMemberBean member;

    @Override
    public String toString() {
        return "DoctorCommentBean{" +
                "comment=" + comment +
                ", commentId='" + commentId + '\'' +
                ", content='" + content + '\'' +
                ", createDate=" + createDate +
                ", doctorId='" + doctorId + '\'' +
                ", isPraise='" + isPraise + '\'' +
                ", praise=" + praise +
                ", projectId='" + projectId + '\'' +
                ", star=" + star +
                ", views=" + views +
                ", member=" + member +
                '}';
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
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

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(String isPraise) {
        this.isPraise = isPraise;
    }

    public int getPraise() {
        return praise;
    }

    public void setPraise(int praise) {
        this.praise = praise;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public CommentMemberBean getMember() {
        return member;
    }

    public void setMember(CommentMemberBean member) {
        this.member = member;
    }
}
