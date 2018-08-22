package me.jessyan.mvparms.demo.mvp.model.entity.score;

import java.io.Serializable;

public class ScorePointBean implements Serializable {
    private long createDate;
    private long inMoney;
    private long outMoney;
    private long point;
    private String type;  // 1:签到；2:连续签到
    private String typeDesc;

    @Override
    public String toString() {
        return "ScorePointBean{" +
                "createDate=" + createDate +
                ", inMoney=" + inMoney +
                ", outMoney=" + outMoney +
                ", point=" + point +
                ", type='" + type + '\'' +
                ", typeDesc='" + typeDesc + '\'' +
                '}';
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getInMoney() {
        return inMoney;
    }

    public void setInMoney(long inMoney) {
        this.inMoney = inMoney;
    }

    public long getOutMoney() {
        return outMoney;
    }

    public void setOutMoney(long outMoney) {
        this.outMoney = outMoney;
    }

    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }
}
