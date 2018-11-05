package me.jessyan.mvparms.demo.mvp.model.entity;

/**
 * Created by guomin on 2018/8/6.
 */

public class Store extends CommonStoreDateType {

    private String province;
    private String city;
    private String county;
    private String storeId;
    private String image;
    private String intro;
    private String isFollow;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }


    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(String isFollow) {
        this.isFollow = isFollow;
    }

    @Override
    public String toString() {
        return "Store{" +
                "province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", county='" + county + '\'' +
                ", storeId='" + storeId + '\'' +
                ", image='" + image + '\'' +
                ", intro='" + intro + '\'' +
                ", isFollow='" + isFollow + '\'' +
                '}';
    }
}
