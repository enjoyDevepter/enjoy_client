package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorBean;

public class DoctorListResponse extends BaseResponse {
    private List<DoctorBean> doctorList;

    @Override
    public String toString() {
        return "DoctorListResponse{" +
                "doctorList=" + doctorList +
                '}';
    }

    public List<DoctorBean> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(List<DoctorBean> doctorList) {
        this.doctorList = doctorList;
    }
}
