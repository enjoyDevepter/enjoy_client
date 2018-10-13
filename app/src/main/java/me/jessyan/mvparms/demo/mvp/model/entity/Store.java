package me.jessyan.mvparms.demo.mvp.model.entity;

/**
 * Created by guomin on 2018/8/6.
 */

public class Store extends CommonStoreDateType {

    private String province;
    private String city;
    private String county;
    private String storeId;

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


    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "Store{" +
                "province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", county='" + county + '\'' +
                ", storeId='" + storeId + '\'' +
                '}';
    }
}
