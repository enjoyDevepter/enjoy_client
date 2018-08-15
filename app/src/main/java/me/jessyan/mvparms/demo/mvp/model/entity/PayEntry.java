package me.jessyan.mvparms.demo.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by guomin on 2018/8/15.
 */

public class PayEntry implements Parcelable {
    public static final Parcelable.Creator<PayEntry> CREATOR = new Parcelable.Creator<PayEntry>() {
        @Override
        public PayEntry createFromParcel(Parcel in) {
            return new PayEntry(in);
        }

        @Override
        public PayEntry[] newArray(int size) {
            return new PayEntry[size];
        }
    };
    private String payId;
    private String type;
    private String name;
    private String image;
    private String extendParams;

    public PayEntry() {

    }

    protected PayEntry(Parcel in) {
        payId = in.readString();
        type = in.readString();
        name = in.readString();
        image = in.readString();
        extendParams = in.readString();
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getExtendParams() {
        return extendParams;
    }

    public void setExtendParams(String extendParams) {
        this.extendParams = extendParams;
    }

    @Override
    public String toString() {
        return "PayEntry{" +
                "payId='" + payId + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", extendParams='" + extendParams + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(payId);
        dest.writeString(type);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeString(extendParams);
    }
}
