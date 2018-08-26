package me.jessyan.mvparms.demo.mvp.model.entity.user.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.Hospital;
import me.jessyan.mvparms.demo.mvp.model.entity.Member;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorBean;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;

public class MyFollowListResponse extends BaseResponse {
    private int nextPageIndex;
    private List<Member> memberList;
    private List<Hospital> hospitalList;
    private List<DoctorBean> doctorList;

    public int getNextPageIndex() {
        return nextPageIndex;
    }

    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }

    public List<Member> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<Member> memberList) {
        this.memberList = memberList;
    }

    public List<Hospital> getHospitalList() {
        return hospitalList;
    }

    public void setHospitalList(List<Hospital> hospitalList) {
        this.hospitalList = hospitalList;
    }

    public List<DoctorBean> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(List<DoctorBean> doctorList) {
        this.doctorList = doctorList;
    }

    @Override
    public String toString() {
        return "MyFollowListResponse{" +
                "nextPageIndex=" + nextPageIndex +
                ", memberList=" + memberList +
                ", hospitalList=" + hospitalList +
                ", doctorList=" + doctorList +
                '}';
    }
}
