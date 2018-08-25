package me.jessyan.mvparms.demo.mvp.model.entity.doctor.request;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

public class LoginUserDoctorInfoRequest extends BaseRequest {
    private final int cmd = 652;
    private String doctorId;
    private String token;

    @Override
    public String toString() {
        return "LoginUserDoctorInfoRequest{" +
                "cmd=" + cmd +
                ", doctorId='" + doctorId + '\'' +
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
