package me.jessyan.mvparms.demo.mvp.model.entity;

/**
 * Created by guomin on 2018/7/29.
 */
public class Ad {

    private String title;
    private String image;
    private String redirectType;
    private String extendParam;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getExtendParam() {
        return extendParam;
    }

    public void setExtendParam(String extendParam) {
        this.extendParam = extendParam;
    }

    public String getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(String redirectType) {
        this.redirectType = redirectType;
    }

    @Override
    public String toString() {
        return "Ad{" +
                "title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", extendParam='" + extendParam + '\'' +
                ", redirectType='" + redirectType + '\'' +
                '}';
    }
}