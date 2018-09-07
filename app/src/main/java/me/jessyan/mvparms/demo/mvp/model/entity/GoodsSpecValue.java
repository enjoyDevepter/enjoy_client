package me.jessyan.mvparms.demo.mvp.model.entity;

/**
 * Created by guomin on 2018/8/14.
 */

public class GoodsSpecValue {
    private String specValueId;
    private String specValueName;


    public String getSpecValueId() {
        return specValueId;
    }

    public void setSpecValueId(String specValueId) {
        this.specValueId = specValueId;
    }

    public String getSpecValueName() {
        return specValueName;
    }

    public void setSpecValueName(String specValueName) {
        this.specValueName = specValueName;
    }

    @Override
    public String toString() {
        return "GoodsSpecValue{" +
                "specValueId='" + specValueId + '\'' +
                ", specValueName='" + specValueName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GoodsSpecValue that = (GoodsSpecValue) o;

        return specValueId.equals(that.specValueId);
    }

    @Override
    public int hashCode() {
        return specValueId.hashCode();
    }
}

