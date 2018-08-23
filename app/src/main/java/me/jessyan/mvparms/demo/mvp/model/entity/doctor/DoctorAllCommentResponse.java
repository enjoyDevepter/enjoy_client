package me.jessyan.mvparms.demo.mvp.model.entity.doctor;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;

public class DoctorAllCommentResponse extends BaseResponse {
    private int nextPageIndex;
    private List<DoctorCommentBean> doctorCommentList;

    @Override
    public String toString() {
        return "DoctorHotCommentResponse{" +
                "nextPageIndex=" + nextPageIndex +
                ", doctorCommentList=" + doctorCommentList +
                '}';
    }

    public int getNextPageIndex() {
        return nextPageIndex;
    }

    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }

    public List<DoctorCommentBean> getDoctorCommentList() {
        return doctorCommentList;
    }

    public void setDoctorCommentList(List<DoctorCommentBean> doctorCommentList) {
        this.doctorCommentList = doctorCommentList;
    }
}
