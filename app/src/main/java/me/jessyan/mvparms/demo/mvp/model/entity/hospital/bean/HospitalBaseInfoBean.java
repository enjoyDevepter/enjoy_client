package me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean;

import me.jessyan.mvparms.demo.mvp.model.entity.CommonStoreDateType;

public class HospitalBaseInfoBean extends CommonStoreDateType {
    private String hospitalId;
    private String province;
    private String city;
    private String county;

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    @Override
    public String toString() {
        return "HospitalBaseInfoBean{" +
                "hospitalId='" + hospitalId + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", county='" + county + '\'' +
                '}';
    }
}
