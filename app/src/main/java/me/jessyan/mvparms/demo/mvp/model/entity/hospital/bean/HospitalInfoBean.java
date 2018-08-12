package me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean;

import java.util.List;

public class HospitalInfoBean {
    private String image;
    private String intro;
    private String isFollow;

    @Override
    public String toString() {
        return "HospitalInfoBean{" +
                "image='" + image + '\'' +
                ", intro='" + intro + '\'' +
                ", isFollow='" + isFollow + '\'' +
                '}';
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(String isFollow) {
        this.isFollow = isFollow;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
