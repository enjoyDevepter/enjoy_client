package me.jessyan.mvparms.demo.mvp.model.entity.user.bean;

import java.io.Serializable;

public class DynamicMessage implements Serializable {
    private String dynamicId;
    private String title;
    private String intro;
    private String status;
    private String createTime;
    private String url;

    public String getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(String dynamicId) {
        this.dynamicId = dynamicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "DynamicMessage{" +
                "dynamicId='" + dynamicId + '\'' +
                ", title='" + title + '\'' +
                ", intro='" + intro + '\'' +
                ", status='" + status + '\'' +
                ", createTime='" + createTime + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
