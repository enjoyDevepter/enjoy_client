package me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean;

import java.io.Serializable;
import java.util.List;

public class DoctorIntorBean implements Serializable {
    private String doctorId;
    private String headImage;
    private String introduce;
    private List<DoctorSkill> doctorSkillList;
    private List<String> dutyList;
    private String name;

    @Override
    public String toString() {
        return "DoctorIntorBean{" +
                "doctorId='" + doctorId + '\'' +
                ", headImage='" + headImage + '\'' +
                ", introduce='" + introduce + '\'' +
                ", doctorSkillList=" + doctorSkillList +
                ", dutyList=" + dutyList +
                ", name='" + name + '\'' +
                '}';
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

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public List<DoctorSkill> getDoctorSkillList() {
        return doctorSkillList;
    }

    public void setDoctorSkillList(List<DoctorSkill> doctorSkillList) {
        this.doctorSkillList = doctorSkillList;
    }

    public List<String> getDutyList() {
        return dutyList;
    }

    public void setDutyList(List<String> dutyList) {
        this.dutyList = dutyList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
