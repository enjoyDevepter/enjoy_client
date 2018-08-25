package me.jessyan.mvparms.demo.mvp.model.entity.doctor.request;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

public class DoctorIntorRequest extends BaseRequest {
    private final int cmd = 655;
    private String doctorId;

    @Override
    public String toString() {
        return "DoctorIntorRequest{" +
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
