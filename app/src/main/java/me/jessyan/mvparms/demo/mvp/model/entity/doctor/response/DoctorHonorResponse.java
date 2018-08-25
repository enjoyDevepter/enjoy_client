package me.jessyan.mvparms.demo.mvp.model.entity.doctor.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorHonorBean;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;

public class DoctorHonorResponse extends BaseResponse {
    private List<DoctorHonorBean> honorList;

    @Override
    public String toString() {
        return "DoctorHonorResponse{" +
                "honorList=" + honorList +
                '}';
    }

    public List<DoctorHonorBean> getHonorList() {
        return honorList;
    }

    public void setHonorList(List<DoctorHonorBean> honorList) {
        this.honorList = honorList;
    }
}
