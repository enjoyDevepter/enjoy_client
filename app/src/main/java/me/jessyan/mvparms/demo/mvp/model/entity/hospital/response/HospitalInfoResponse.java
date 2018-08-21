package me.jessyan.mvparms.demo.mvp.model.entity.hospital.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean.HospitalEnvBean;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean.HospitalInfoBean;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;

public class HospitalInfoResponse extends BaseResponse {
    private HospitalInfoBean hospital;
    private List<HospitalEnvBean> hospitalEnvList;

    @Override
    public String toString() {
        return "HospitalInfoResponse{" +
                "hospital=" + hospital +
                ", hospitalEnvList=" + hospitalEnvList +
                '}';
    }

    public HospitalInfoBean getHospital() {
        return hospital;
    }

    public void setHospital(HospitalInfoBean hospital) {
        this.hospital = hospital;
    }

    public List<HospitalEnvBean> getHospitalEnvList() {
        return hospitalEnvList;
    }

    public void setHospitalEnvList(List<HospitalEnvBean> hospitalEnvList) {
        this.hospitalEnvList = hospitalEnvList;
    }
}
