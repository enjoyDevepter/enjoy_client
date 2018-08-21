package me.jessyan.mvparms.demo.mvp.model.entity.doctor;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

public class DoctorInfoRequest extends BaseRequest {
    private final int cmd = 651;
    private String doctorId;

    @Override
    public String toString() {
        return "DoctorInfoRequest{" +
                "cmd=" + cmd +
                ", doctorId='" + doctorId + '\'' +
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
}
