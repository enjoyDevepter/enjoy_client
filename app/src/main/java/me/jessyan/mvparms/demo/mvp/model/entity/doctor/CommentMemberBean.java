package me.jessyan.mvparms.demo.mvp.model.entity.doctor;

import java.io.Serializable;

public class CommentMemberBean implements Serializable {
    private String headImage;
    private String memberId;
    private String nickName;

    @Override
    public String toString() {
        return "CommentMemberBean{" +
                "headImage='" + headImage + '\'' +
                ", memberId='" + memberId + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
