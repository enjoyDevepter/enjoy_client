package me.jessyan.mvparms.demo.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by guomin on 2018/8/16.
 */
public class HAppointmentsTime implements Parcelable {
    public static final Creator<HAppointmentsTime> CREATOR = new Creator<HAppointmentsTime>() {
        @Override
        public HAppointmentsTime createFromParcel(Parcel in) {
            return new HAppointmentsTime(in);
        }

        @Override
        public HAppointmentsTime[] newArray(int size) {
            return new HAppointmentsTime[size];
        }
    };
    private String time;
    private String full;
    private boolean choice;

    public HAppointmentsTime() {

    }

    protected HAppointmentsTime(Parcel in) {
        time = in.readString();
        full = in.readString();
        choice = in.readByte() != 0;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }

    public boolean isChoice() {
        return choice;
    }

    public void setChoice(boolean choice) {
        this.choice = choice;
    }

    @Override
    public String toString() {
        return "HAppointmentsTime{" +
                "time='" + time + '\'' +
                ", full='" + full + '\'' +
                ", choice=" + choice +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(time);
        dest.writeString(full);
        dest.writeByte((byte) (choice ? 1 : 0));
    }
}
