package me.jessyan.mvparms.demo.mvp.model.entity;

/**
 * Created by guomin on 2018/7/29.
 */
public class Module {

    private String name;
    private String image;
    private String type;
    private String redirectType;
    private String body;
    private String extendParam;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(String redirectType) {
        this.redirectType = redirectType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getExtendParam() {
        return extendParam;
    }

    public void setExtendParam(String extendParam) {
        this.extendParam = extendParam;
    }

    @Override
    public String toString() {
        return "Module{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", type='" + type + '\'' +
                ", redirectType='" + redirectType + '\'' +
                ", body='" + body + '\'' +
                ", extendParam='" + extendParam + '\'' +
                '}';
    }
}
