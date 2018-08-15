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
}

