package me.jessyan.mvparms.demo.mvp.model.entity.doctor;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

public class LikeDoctorRequest extends BaseRequest {
    private final int cmd = 665;
    private String doctorId;
    private String token;

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
