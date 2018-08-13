package me.jessyan.mvparms.demo.mvp.model.entity;


/**
 * 日志类型
 */
public class DiaryNavi {
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
        return "DiaryNavi{" +
                "title='" + title + '\'' +
                ", redirectType='" + redirectType + '\'' +
                ", extendParam='" + extendParam + '\'' +
                '}';
    }
}
