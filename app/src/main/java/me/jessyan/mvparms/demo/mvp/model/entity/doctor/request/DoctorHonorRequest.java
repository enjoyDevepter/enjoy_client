package me.jessyan.mvparms.demo.mvp.model.entity.doctor.request;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

/**请求医生证书列表*/
public class DoctorHonorRequest extends BaseRequest {
    private final int cmd = 657;
    private String doctorId;

    @Override
    public String toString() {
        return "DoctorHonorRequest{" +
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
