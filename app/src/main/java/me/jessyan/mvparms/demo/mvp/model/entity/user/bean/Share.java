package me.jessyan.mvparms.demo.mvp.model.entity.user.bean;

/**
 * Created by guomin on 2018/8/28.
 */

public class Share {
    private String title;
    private String intro;
    private String image;
    private String url;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Share{" +
                "title='" + title + '\'' +
                ", intro='" + intro + '\'' +
                ", image='" + image + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
