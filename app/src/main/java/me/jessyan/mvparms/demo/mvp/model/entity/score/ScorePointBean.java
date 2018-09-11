package me.jessyan.mvparms.demo.mvp.model.entity.score;

import java.io.Serializable;

public class ScorePointBean implements Serializable {
    private String createDate;
    private long inMoney;
    private long outMoney;
    private String desc;

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "ScorePointBean{" +
                "createDate='" + createDate + '\'' +
                ", inMoney=" + inMoney +
                ", outMoney=" + outMoney +
                ", desc='" + desc + '\'' +
                '}';
    }
}
