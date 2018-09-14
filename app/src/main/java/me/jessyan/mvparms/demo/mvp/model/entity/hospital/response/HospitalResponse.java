package me.jessyan.mvparms.demo.mvp.model.entity.hospital.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.Hospital;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;

public class HospitalResponse extends BaseResponse {
    private List<Hospital> hospitalList;
    private int nextPageIndex;

    @Override
    public String toString() {
        return "HospitalListResponse{" +
                "hospitalList=" + hospitalList +
                ", nextPageIndex=" + nextPageIndex +
                '}';
    }

    public List<Hospital> getHospitalList() {
        return hospitalList;
    }

    public void setHospitalList(List<Hospital> hospitalList) {
        this.hospitalList = hospitalList;
    }

    public int getNextPageIndex() {
        return nextPageIndex;
    }

    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }
}
