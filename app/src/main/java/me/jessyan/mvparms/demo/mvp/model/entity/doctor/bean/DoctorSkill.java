package me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean;

import java.io.Serializable;

public class DoctorSkill implements Serializable{
    private String projectId;
    private String projectName;

    @Override
    public String toString() {
        return "DoctorSkill{" +
                "projectId='" + projectId + '\'' +
                ", projectName='" + projectName + '\'' +
                '}';
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
