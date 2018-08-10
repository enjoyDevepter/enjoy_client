package me.jessyan.mvparms.demo.mvp.model.entity;

import java.util.List;

/**
 * Created by guomin on 2018/7/29.
 */
public class AreaAddress {

    private String areaId;
    private String name;
    private String code;
    private String type;
    private List<AreaAddress> areaList;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<AreaAddress> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<AreaAddress> areaList) {
        this.areaList = areaList;
    }

    @Override
    public String toString() {
        return "AreaAddress{" +
                "areaId='" + areaId + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", type='" + type + '\'' +
                ", areaList=" + areaList +
                '}';
    }
}