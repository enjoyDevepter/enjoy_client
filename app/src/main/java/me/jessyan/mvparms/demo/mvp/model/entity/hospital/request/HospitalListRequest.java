package me.jessyan.mvparms.demo.mvp.model.entity.hospital.request;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean.OrderBy;
import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

public class HospitalListRequest extends BaseRequest {
    private int pageIndex;
    private int pageSize;
    private String provinceId;
    private String cityId;
    private String countyId;
    private String lon;
    private String lat;
    private String specValueId;
    private List<OrderBy> orderBys;

    @Override
    public String toString() {
        return "HospitalListRequest{" +
                "pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                ", provinceId='" + provinceId + '\'' +
                ", cityId='" + cityId + '\'' +
                ", countyId='" + countyId + '\'' +
                ", lon='" + lon + '\'' +
                ", lat='" + lat + '\'' +
                ", specValueId='" + specValueId + '\'' +
                ", orderBys=" + orderBys +
                '}';
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCountyId() {
        return countyId;
    }

    public void setCountyId(String countyId) {
        this.countyId = countyId;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getSpecValueId() {
        return specValueId;
    }

    public void setSpecValueId(String specValueId) {
        this.specValueId = specValueId;
    }

    public List<OrderBy> getOrderBys() {
        return orderBys;
    }

    public void setOrderBys(List<OrderBy> orderBys) {
        this.orderBys = orderBys;
    }
}
