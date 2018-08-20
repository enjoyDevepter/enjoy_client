package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.appointment.Appointment;

/**
 * Created by guomin on 2018/7/28.
 */

public class AppointmentResponse extends BaseResponse {

    private List<Appointment> orderProjectDetailList;
    private int nextPageIndex;

    public List<Appointment> getOrderProjectDetailList() {
        return orderProjectDetailList;
    }

    public void setOrderProjectDetailList(List<Appointment> orderProjectDetailList) {
        this.orderProjectDetailList = orderProjectDetailList;
    }

    public int getNextPageIndex() {
        return nextPageIndex;
    }

    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }

    @Override
    public String toString() {
        return "AppointmentResponse{" +
                "orderProjectDetailList=" + orderProjectDetailList +
                ", nextPageIndex=" + nextPageIndex +
                '}';
    }
}
