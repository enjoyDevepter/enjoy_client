package me.jessyan.mvparms.demo.mvp.model.entity.doctor;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

public class CommentDoctorRequest extends BaseRequest {
    private final int cmd = 654;
    private String doctorId;
    private String projectId;
    private int star;
    private String content;
    private String token;

    @Override
    public String toString() {
        return "CommentDoctorRequest{" +
                "cmd=" + cmd +
                ", doctorId='" + doctorId + '\'' +
                ", projectId='" + projectId + '\'' +
                ", star=" + star +
                ", content='" + content + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    public int getCmd() {
        return cmd;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
