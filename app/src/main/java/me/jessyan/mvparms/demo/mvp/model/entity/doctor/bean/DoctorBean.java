package me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean;

import java.io.Serializable;
import java.util.List;

public class DoctorBean implements Serializable {
    private String doctorId;
    private String headImage;
    private String name;
    private int star;
    private String followDate;
    private String isFollow;
    private List<DoctorSkill> doctorSkillList;
    private List<HospitalBean> hospitalList;

    private int comment;
    private String isPraise;  // 1:是0:否

    @Override
    public String toString() {
        return "DoctorBean{" +
                "doctorId='" + doctorId + '\'' +
                ", headImage='" + headImage + '\'' +
                ", name='" + name + '\'' +
                ", star=" + star +
                ", followDate='" + followDate + '\'' +
                ", isFollow='" + isFollow + '\'' +
                ", doctorSkillList=" + doctorSkillList +
                ", hospitalList=" + hospitalList +
                ", comment=" + comment +
                ", isPraise='" + isPraise + '\'' +
                '}';
    }

    public String getFollowDate() {
        return followDate;
    }

    public void setFollowDate(String followDate) {
        this.followDate = followDate;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public List<DoctorSkill> getDoctorSkillList() {
        return doctorSkillList;
    }

    public void setDoctorSkillList(List<DoctorSkill> doctorSkillList) {
        this.doctorSkillList = doctorSkillList;
    }

    public List<HospitalBean> getHospitalList() {
        return hospitalList;
    }

    public void setHospitalList(List<HospitalBean> hospitalList) {
        this.hospitalList = hospitalList;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public String getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(String isPraise) {
        this.isPraise = isPraise;
    }

    public String getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(String isFollow) {
        this.isFollow = isFollow;
    }
}
