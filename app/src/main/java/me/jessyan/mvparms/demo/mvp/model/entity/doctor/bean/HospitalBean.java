package me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean;

import java.io.Serializable;

public class HospitalBean implements Serializable {
    private String hospitalId;
    private String name;
    private String image;
    private String followDate;

    @Override
    public String toString() {
        return "HospitalBean{" +
                "hospitalId='" + hospitalId + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", followDate='" + followDate + '\'' +
                '}';
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFollowDate() {
        return followDate;
    }

    public void setFollowDate(String followDate) {
        this.followDate = followDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }
}
