package me.jessyan.mvparms.demo.mvp.model.entity.doctor.request;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

public class LoginUserDoctorHotCommentRequest extends BaseRequest {
    private final int cmd = 659;
    private String doctorId;
    private int pageIndex = 1;  // 热门评论暂定只加载一页
    private final int pageSize = 10;
    private String token;

    @Override
    public String toString() {
        return "LoginUserDoctorHotCommentRequest{" +
                "cmd=" + cmd +
                ", doctorId='" + doctorId + '\'' +
                ", pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                ", token='" + token + '\'' +
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
