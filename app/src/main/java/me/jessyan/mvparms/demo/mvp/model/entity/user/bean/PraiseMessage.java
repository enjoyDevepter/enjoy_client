package me.jessyan.mvparms.demo.mvp.model.entity.user.bean;

import java.io.Serializable;

public class PraiseMessage implements Serializable {
    private String diaryId;
    private String name;
    private String memberId;
    private String headImage;
    private String createTime;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDiaryId() {
        return diaryId;
    }

    public void setDiaryId(String diaryId) {
        this.diaryId = diaryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "PraiseMessage{" +
                "diaryId='" + diaryId + '\'' +
                ", name='" + name + '\'' +
                ", memberId='" + memberId + '\'' +
                ", headImage='" + headImage + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
