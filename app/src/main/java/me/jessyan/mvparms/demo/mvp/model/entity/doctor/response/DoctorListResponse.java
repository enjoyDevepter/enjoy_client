package me.jessyan.mvparms.demo.mvp.model.entity.doctor.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorBean;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;

public class DoctorListResponse extends BaseResponse {
    private List<DoctorBean> doctorList;
    private int nextPageIndex;

    @Override
    public String toString() {
        return "DoctorListResponse{" +
                "doctorList=" + doctorList +
                ", nextPageIndex=" + nextPageIndex +
                '}';
    }

    public List<DoctorBean> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(List<DoctorBean> doctorList) {
        this.doctorList = doctorList;
    }

    public int getNextPageIndex() {
        return nextPageIndex;
    }

    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }
}
