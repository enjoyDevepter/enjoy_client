package me.jessyan.mvparms.demo.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.HAppointmentsTime;

/**
 * Created by guomin on 2018/8/14.
 */

public class HAppointments implements Parcelable {
    public static final Creator<HAppointments> CREATOR = new Creator<HAppointments>() {
        @Override
        public HAppointments createFromParcel(Parcel in) {
            return new HAppointments(in);
        }

        @Override
        public HAppointments[] newArray(int size) {
            return new HAppointments[size];
        }
    };
    private String date;
    private boolean choice;
    private List<HAppointmentsTime> reservationTimeList;

    protected HAppointments(Parcel in) {
        date = in.readString();
        choice = in.readByte() != 0;
        reservationTimeList = in.createTypedArrayList(HAppointmentsTime.CREATOR);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isChoice() {
        return choice;
    }

    public void setChoice(boolean choice) {
        this.choice = choice;
    }

    public List<HAppointmentsTime> getReservationTimeList() {
        return reservationTimeList;
    }

    public void setReservationTimeList(List<HAppointmentsTime> reservationTimeList) {
        this.reservationTimeList = reservationTimeList;
    }

    @Override
    public String toString() {
        return "HAppointments{" +
                "date='" + date + '\'' +
                ", choice=" + choice +
                ", reservationTimeList=" + reservationTimeList +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeByte((byte) (choice ? 1 : 0));
        dest.writeTypedList(reservationTimeList);
    }
}
