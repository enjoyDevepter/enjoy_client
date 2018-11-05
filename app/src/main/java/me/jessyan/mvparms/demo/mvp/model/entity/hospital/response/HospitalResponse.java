package me.jessyan.mvparms.demo.mvp.model.entity.hospital.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.Hospital;
import me.jessyan.mvparms.demo.mvp.model.entity.Store;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;

public class HospitalResponse extends BaseResponse {
    private List<Hospital> hospitalList;
    private List<Store> storeList;
    private int nextPageIndex;

    @Override
    public String toString() {
        return "HospitalResponse{" +
                "hospitalList=" + hospitalList +
                ", storeList=" + storeList +
                ", nextPageIndex=" + nextPageIndex +
                '}';
    }

    public List<Store> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<Store> storeList) {
        this.storeList = storeList;
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
