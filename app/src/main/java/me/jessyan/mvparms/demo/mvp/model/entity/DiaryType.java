package me.jessyan.mvparms.demo.mvp.model.entity;


/**
 * 日志类型
 */
public class DiaryType {
    private String name;
    private String typeId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    @Override
    public String toString() {
        return "DiaryType{" +
                "name='" + name + '\'' +
                ", typeId='" + typeId + '\'' +
                '}';
    }
}
