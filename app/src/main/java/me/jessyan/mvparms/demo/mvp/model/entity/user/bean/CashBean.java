package me.jessyan.mvparms.demo.mvp.model.entity.user.bean;

import java.io.Serializable;

public class CashBean implements Serializable {
    private long inMoney;
    private long outMoney;
    private long createDate;
    private String desc;

    @Override
    public String toString() {
        return "CashBean{" +
                "inMoney=" + inMoney +
                ", outMoney=" + outMoney +
                ", createDate=" + createDate +
                ", desc='" + desc + '\'' +
                '}';
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

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
