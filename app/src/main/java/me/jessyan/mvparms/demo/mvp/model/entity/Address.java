package me.jessyan.mvparms.demo.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by guomin on 2018/7/29.
 */
public class Address implements Parcelable {

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
    private String address;
    private String city;
    private String cityName;
    private String county;
    private String countyName;
    private String province;
    private String provinceName;
    private String addressId;
    private String isDefaultIn = "0";
    private String phone;
    private String receiverName;
    private String zipcode;

    public Address() {
    }

    protected Address(Parcel in) {
        address = in.readString();
        city = in.readString();
        cityName = in.readString();
        county = in.readString();
        countyName = in.readString();
        province = in.readString();
        provinceName = in.readString();
        addressId = in.readString();
        isDefaultIn = in.readString();
        phone = in.readString();
        receiverName = in.readString();
        zipcode = in.readString();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getIsDefaultIn() {
        return isDefaultIn;
    }

    public void setIsDefaultIn(String isDefaultIn) {
        this.isDefaultIn = isDefaultIn;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    @Override
    public String toString() {
        return "Address{" +
                "address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", cityName='" + cityName + '\'' +
                ", county='" + county + '\'' +
                ", countyName='" + countyName + '\'' +
                ", province='" + province + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", addressId='" + addressId + '\'' +
                ", isDefaultIn='" + isDefaultIn + '\'' +
                ", phone='" + phone + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", zipcode='" + zipcode + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(cityName);
        dest.writeString(county);
        dest.writeString(countyName);
        dest.writeString(province);
        dest.writeString(provinceName);
        dest.writeString(addressId);
        dest.writeString(isDefaultIn);
        dest.writeString(phone);
        dest.writeString(receiverName);
        dest.writeString(zipcode);
    }
}