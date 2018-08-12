package me.jessyan.mvparms.demo.mvp.model.entity.hospital.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean.HospitalBaseInfoBean;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;

public class HospitalListResponse extends BaseResponse {
    private List<HospitalBaseInfoBean> hospitalList;
    private int nextPageIndex;

    @Override
    public String toString() {
        return "HospitalListResponse{" +
                "hospitalList=" + hospitalList +
                ", nextPageIndex=" + nextPageIndex +
                '}';
    }

    public List<HospitalBaseInfoBean> getHospitalList() {
        return hospitalList;
    }

    public void setHospitalList(List<HospitalBaseInfoBean> hospitalList) {
        this.hospitalList = hospitalList;
    }

    public int getNextPageIndex() {
        return nextPageIndex;
    }

    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }
}
