package me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean;

import java.io.Serializable;

public class DoctorIdentificationBean implements Serializable {
    private String image;
    private String title;

    @Override
    public String toString() {
        return "DoctorIdentificationBean{" +
                "image='" + image + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
