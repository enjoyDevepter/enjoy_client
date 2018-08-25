package me.jessyan.mvparms.demo.mvp.model.entity.doctor.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorCommentReplyBean;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;

public class GetDoctorCommentReplyPageResponse extends BaseResponse {
    private int nextPageIndex;
    private List<DoctorCommentReplyBean> doctorCommentReplyList;

    @Override
    public String toString() {
        return "GetDoctorCommentReplyPageResponse{" +
                "nextPageIndex=" + nextPageIndex +
                ", doctorCommentReplyList=" + doctorCommentReplyList +
                '}';
    }

    public int getNextPageIndex() {
        return nextPageIndex;
    }

    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }

    public List<DoctorCommentReplyBean> getDoctorCommentReplyList() {
        return doctorCommentReplyList;
    }

    public void setDoctorCommentReplyList(List<DoctorCommentReplyBean> doctorCommentReplyList) {
        this.doctorCommentReplyList = doctorCommentReplyList;
    }
}
