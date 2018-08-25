package me.jessyan.mvparms.demo.mvp.model.entity;

import java.io.Serializable;

/**
 * Created by guomin on 2018/7/29.
 */
public class Area implements Serializable {
    private String code;
    private String id;
    private String name;
    private String parentId;
    private String type;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Area{" +
                "code='" + code + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", parentId='" + parentId + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}