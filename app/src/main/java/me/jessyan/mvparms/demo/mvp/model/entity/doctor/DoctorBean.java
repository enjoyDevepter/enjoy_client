package me.jessyan.mvparms.demo.mvp.model.entity.doctor;

import java.util.List;

public class DoctorBean {
    private String doctorId;
    private String headImage;
    private String name;
    private int star;
    private List<DoctorSkill> doctorSkillList;
    private HospitalBean hospitalBean;

    @Override
    public String toString() {
        return "DoctorBean{" +
                "doctorId='" + doctorId + '\'' +
                ", headImage='" + headImage + '\'' +
                ", name='" + name + '\'' +
                ", star=" + star +
                ", doctorSkillList=" + doctorSkillList +
                ", hospitalBean=" + hospitalBean +
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

    public HospitalBean getHospitalBean() {
        return hospitalBean;
    }

    public void setHospitalBean(HospitalBean hospitalBean) {
        this.hospitalBean = hospitalBean;
    }
}