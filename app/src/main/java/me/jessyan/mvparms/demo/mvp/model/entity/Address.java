package me.jessyan.mvparms.demo.mvp.model.entity;

/**
 * Created by guomin on 2018/7/29.
 */
public class Address {

    private String id;
    private String county;
    private String address;
    private String city;
    private String isDefaultIn;
    private String phone;
    private String receiverName;
    private String zipcode;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
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
                "id='" + id + '\'' +
                ", county='" + county + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", isDefaultIn='" + isDefaultIn + '\'' +
                ", phone='" + phone + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", zipcode='" + zipcode + '\'' +
                '}';
    }
}