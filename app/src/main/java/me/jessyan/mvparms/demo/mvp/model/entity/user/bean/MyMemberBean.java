package me.jessyan.mvparms.demo.mvp.model.entity.user.bean;

import java.io.Serializable;

/**下属会员对象*/
public class MyMemberBean implements Serializable {
    private String userName;
    private String headImage;
    private String regTime;

    @Override
    public String toString() {
        return "MyMemberBean{" +
                "userName='" + userName + '\'' +
                ", headImage='" + headImage + '\'' +
                ", regTime='" + regTime + '\'' +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }
}
