package me.jessyan.mvparms.demo.mvp.model.entity;

/**
 * Created by guomin on 2018/7/29.
 */
public class NaviInfo {

    private String title;
    private String redirectType;
    private String extendParam;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(String redirectType) {
        this.redirectType = redirectType;
    }

    public String getExtendParam() {
        return extendParam;
    }

    public void setExtendParam(String extendParam) {
        this.extendParam = extendParam;
    }

    @Override
    public String toString() {
        return "NaviTwo{" +
                "title='" + title + '\'' +
                ", redirectType='" + redirectType + '\'' +
                ", extendParam='" + extendParam + '\'' +
                '}';
    }
}