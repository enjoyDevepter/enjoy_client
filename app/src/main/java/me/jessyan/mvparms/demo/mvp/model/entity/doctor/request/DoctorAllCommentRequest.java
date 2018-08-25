package me.jessyan.mvparms.demo.mvp.model.entity.doctor.request;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

public class DoctorAllCommentRequest extends BaseRequest {
    private final int cmd = 660;
    private String doctorId;
    private int pageIndex = 1;  // 热门评论暂定只加载一页
    private final int pageSize = 10;

    @Override
    public String toString() {
        return "DoctorHotCommentRequest{" +
                "cmd=" + cmd +
                ", doctorId='" + doctorId + '\'' +
                ", pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                '}';
    }

    public int getCmd() {
        return cmd;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
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
}
