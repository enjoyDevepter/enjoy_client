package me.jessyan.mvparms.demo.mvp.model.entity.doctor;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;

public class DoctorPaperResponse extends BaseResponse {
    private List<DoctorIdentificationBean> identificationList;

    @Override
    public String toString() {
        return "DoctorPaperResponse{" +
                "identificationList=" + identificationList +
                '}';
    }

    public List<DoctorIdentificationBean> getIdentificationList() {
        return identificationList;
    }

    public void setIdentificationList(List<DoctorIdentificationBean> identificationList) {
        this.identificationList = identificationList;
    }
}
