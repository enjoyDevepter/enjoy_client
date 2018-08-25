package me.jessyan.mvparms.demo.mvp.model.entity.user.bean;

import java.io.Serializable;

/**会员等级*/
public class PointLevel implements Serializable {
    private String pointLevelId;  // 1:普通会员,2:黄金会员,3:白金会员,4:钻石会员,5:金牌会员,6:超级会员
    private String pointLevelName;

    @Override
    public String toString() {
        return "PointLevel{" +
                "pointLevelId='" + pointLevelId + '\'' +
                ", pointLevelName='" + pointLevelName + '\'' +
                '}';
    }

    public String getPointLevelId() {
        return pointLevelId;
    }

    public void setPointLevelId(String pointLevelId) {
        this.pointLevelId = pointLevelId;
    }

    public String getPointLevelName() {
        return pointLevelName;
    }

    public void setPointLevelName(String pointLevelName) {
        this.pointLevelName = pointLevelName;
    }
}
