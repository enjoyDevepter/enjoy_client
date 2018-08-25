package me.jessyan.mvparms.demo.mvp.model.entity.doctor.response;

import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorIntorBean;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;

public class DoctorIntorResponse extends BaseResponse {
    private DoctorIntorBean doctor;

    @Override
    public String toString() {
        return "DoctorIntorResponse{" +
                "doctor=" + doctor +
                '}';
    }

    public DoctorIntorBean getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorIntorBean doctor) {
        this.doctor = doctor;
    }
}
