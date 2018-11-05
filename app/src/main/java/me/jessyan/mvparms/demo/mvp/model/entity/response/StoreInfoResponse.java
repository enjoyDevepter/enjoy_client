package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.Store;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean.HospitalEnvBean;

public class StoreInfoResponse extends BaseResponse {
    private Store store;
    private List<HospitalEnvBean> storeEnvList;

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public List<HospitalEnvBean> getStoreEnvList() {
        return storeEnvList;
    }

    public void setStoreEnvList(List<HospitalEnvBean> storeEnvList) {
        this.storeEnvList = storeEnvList;
    }

    @Override
    public String toString() {
        return "StoreInfoResponse{" +
                "store=" + store +
                ", storeEnvList=" + storeEnvList +
                '}';
    }
}
