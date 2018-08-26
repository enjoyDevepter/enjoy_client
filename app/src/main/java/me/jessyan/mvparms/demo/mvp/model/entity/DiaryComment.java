package me.jessyan.mvparms.demo.mvp.model.entity;


/**
 * 日志
 */
public class DiaryComment {

    private String commentDate;
    private String content;
    private Member member;

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @Override
    public String toString() {
        return "DiaryComment{" +
                "commentDate='" + commentDate + '\'' +
                ", content='" + content + '\'' +
                ", member=" + member +
                '}';
    }
}
