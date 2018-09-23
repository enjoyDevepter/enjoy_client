package me.jessyan.mvparms.demo.mvp.model.entity.user.bean;

/**
 * Created by guomin on 2018/9/22.
 */

public class GrowthInfo {

    private String name;
    private long growth;
    private double percent;
    private String createDate;
    private long inGrowth;
    private String type;
    private String typeDesc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getGrowth() {
        return growth;
    }

    public void setGrowth(long growth) {
        this.growth = growth;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public long getInGrowth() {
        return inGrowth;
    }

    public void setInGrowth(long inGrowth) {
        this.inGrowth = inGrowth;
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

    @Override
    public String toString() {
        return "GrowthInfo{" +
                "name='" + name + '\'' +
                ", growth=" + growth +
                ", percent=" + percent +
                ", createDate='" + createDate + '\'' +
                ", inGrowth=" + inGrowth +
                ", type='" + type + '\'' +
                ", typeDesc='" + typeDesc + '\'' +
                '}';
    }
}
