package me.jessyan.mvparms.demo.mvp.model.entity;

/**
 * Created by guomin on 2018/8/6.
 */

public class Store extends CommonStoreDateType {

    private String id;
    private String province;
    private String city;
    private String county;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        return "Store{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", province='" + province + '\'' +
                ", distance='" + distance + '\'' +
                ", city='" + city + '\'' +
                ", distanceDesc='" + distanceDesc + '\'' +
                ", county='" + county + '\'' +
                ", check=" + check +
                '}';
    }
}
