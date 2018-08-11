package me.jessyan.mvparms.demo.mvp.model.entity.request;

public class DoctorListRequest extends BaseRequest {
    private String hospitalId;
    private int pageIndex;
    private int pageSize;

    @Override
    public String toString() {
        return "DoctorListRequest{" +
                "hospitalId='" + hospitalId + '\'' +
                ", pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                '}';
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
