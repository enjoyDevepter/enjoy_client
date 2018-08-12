package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.hospital.HospitalBean;

public class HospitalListResponse extends BaseResponse {
    private List<HospitalBean> hospitalList;
    private int nextPageIndex;

    @Override
    public String toString() {
        return "HospitalListResponse{" +
                "hospitalList=" + hospitalList +
                ", nextPageIndex=" + nextPageIndex +
                '}';
    }

    public List<HospitalBean> getHospitalList() {
        return hospitalList;
    }

    public void setHospitalList(List<HospitalBean> hospitalList) {
        this.hospitalList = hospitalList;
    }

    public int getNextPageIndex() {
        return nextPageIndex;
    }

    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }
}
