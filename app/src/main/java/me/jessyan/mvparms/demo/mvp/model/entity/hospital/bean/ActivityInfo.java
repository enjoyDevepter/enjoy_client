package me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean;

/**
 * Created by guomin on 2018/10/13.
 */

public class ActivityInfo {

    private String activityId;
    private String title;
    private String createTime;
    private String content;
    private String image;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ActivityInfo{" +
                "activityId='" + activityId + '\'' +
                ", title='" + title + '\'' +
                ", createTime='" + createTime + '\'' +
                ", content='" + content + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}

