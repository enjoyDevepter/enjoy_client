package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.HAppointments;

/**
 * Created by guomin on 2018/7/28.
 */

public class GetAppointmentTimeResponse extends BaseResponse {

    private List<HAppointments> reservationDateList;
    private int nextPageIndex;

    public List<HAppointments> getReservationDateList() {
        return reservationDateList;
    }

    public void setReservationDateList(List<HAppointments> reservationDateList) {
        this.reservationDateList = reservationDateList;
    }

    public int getNextPageIndex() {
        return nextPageIndex;
    }

    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }

    @Override
    public String toString() {
        return "GetAppointmentTimeResponse{" +
                "reservationDateList=" + reservationDateList +
                ", nextPageIndex=" + nextPageIndex +
                '}';
    }
}
