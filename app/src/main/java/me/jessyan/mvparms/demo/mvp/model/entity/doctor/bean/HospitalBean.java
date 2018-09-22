package me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class HospitalBean implements Parcelable {
    public static final Creator<HospitalBean> CREATOR = new Creator<HospitalBean>() {
        @Override
        public HospitalBean createFromParcel(Parcel in) {
            return new HospitalBean(in);
        }

        @Override
        public HospitalBean[] newArray(int size) {
            return new HospitalBean[size];
        }
    };
    private String address;
    private String hospitalId;
    private String name;
    private String image;
    private String followDate;

    public HospitalBean() {

    }

    protected HospitalBean(Parcel in) {
        address = in.readString();
        hospitalId = in.readString();
        name = in.readString();
        image = in.readString();
        followDate = in.readString();
    }

    @Override
    public String toString() {
        return "HospitalBean{" +
                "address='" + address + '\'' +
                ", hospitalId='" + hospitalId + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", followDate='" + followDate + '\'' +
                '}';
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFollowDate() {
        return followDate;
    }

    public void setFollowDate(String followDate) {
        this.followDate = followDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(hospitalId);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeString(followDate);
    }
}
