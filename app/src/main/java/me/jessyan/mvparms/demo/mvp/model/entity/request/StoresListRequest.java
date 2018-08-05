package me.jessyan.mvparms.demo.mvp.model.entity.request;

import java.util.List;

/**
 * Created by guomin on 2018/7/25.
 */

public class StoresListRequest extends BaseRequest {
    private int cmd = 701;
    private int pageIndex;
    private int pageSize;
    private String provinceId;
    private String cityId;
    private String countyId;
    private String lon;
    private String lat;
    private List<OrderBy> orderBys;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
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

    public List<OrderBy> getOrderBys() {
        return orderBys;
    }

    public void setOrderBys(List<OrderBy> orderBys) {
        this.orderBys = orderBys;
    }

    @Override
    public String toString() {
        return "StoresListRequest{" +
                "cmd=" + cmd +
                ", pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                ", provinceId='" + provinceId + '\'' +
                ", cityId='" + cityId + '\'' +
                ", countyId='" + countyId + '\'' +
                ", lon='" + lon + '\'' +
                ", lat='" + lat + '\'' +
                ", orderBys=" + orderBys +
                '}';
    }

    public class OrderBy {
        private String field;
        private String asc;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getAsc() {
            return asc;
        }

        public void setAsc(String asc) {
            this.asc = asc;
        }

        @Override
        public String toString() {
            return "OrderBy{" +
                    "field='" + field + '\'' +
                    ", asc='" + asc + '\'' +
                    '}';
        }
    }

}