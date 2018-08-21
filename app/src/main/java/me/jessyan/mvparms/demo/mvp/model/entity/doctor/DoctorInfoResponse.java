package me.jessyan.mvparms.demo.mvp.model.entity.doctor;

import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;

public class DoctorInfoResponse extends BaseResponse {
    private DoctorBean doctor;

    @Override
    public String toString() {
        return "DoctorInfoResponse{" +
                "doctor=" + doctor +
                '}';
    }

    public DoctorBean getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorBean doctor) {
        this.doctor = doctor;
    }
}
