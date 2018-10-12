package me.jessyan.mvparms.demo.mvp.model.entity.doctor.request;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean.OrderBy;
import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

public class DoctorListRequest extends BaseRequest {
    private int cmd = 650;
    private String province;
    private String city;
    private String county;
    private String hospitalId;
    private String firstCategoryId;
    private String secondCategoryId;
    private String categoryId;
    private int pageIndex = 1;
    private int pageSize = 10;
    private List<OrderBy> orderBys;

    @Override
    public String toString() {
        return "DoctorListRequest{" +
                "cmd=" + cmd +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", county='" + county + '\'' +
                ", hospitalId='" + hospitalId + '\'' +
                ", firstCategoryId='" + firstCategoryId + '\'' +
                ", secondCategoryId='" + secondCategoryId + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                ", orderBys=" + orderBys +
                '}';
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

    public String getProvince() {

        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getFirstCategoryId() {
        return firstCategoryId;
    }

    public void setFirstCategoryId(String firstCategoryId) {
        this.firstCategoryId = firstCategoryId;
    }

    public String getSecondCategoryId() {
        return secondCategoryId;
    }

    public void setSecondCategoryId(String secondCategoryId) {
        this.secondCategoryId = secondCategoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public List<OrderBy> getOrderBys() {
        return orderBys;
    }

    public void setOrderBys(List<OrderBy> orderBys) {
        this.orderBys = orderBys;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
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
}
