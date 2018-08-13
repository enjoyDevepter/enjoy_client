package me.jessyan.mvparms.demo.mvp.model.entity;


/**
 * 日志
 */
public class DiaryMember {
    private String headImage;
    private String isFollow;
    private String memberId;
    private String nickName;

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(String isFollow) {
        this.isFollow = isFollow;
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

    @Override
    public String toString() {
        return "Member{" +
                "headImage='" + headImage + '\'' +
                ", isFollow='" + isFollow + '\'' +
                ", memberId='" + memberId + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}
