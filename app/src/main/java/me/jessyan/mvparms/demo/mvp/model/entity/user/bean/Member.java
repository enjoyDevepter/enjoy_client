package me.jessyan.mvparms.demo.mvp.model.entity.user.bean;

import java.io.Serializable;
import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.Area;

public class Member implements Serializable {
    private String userName;
    private String nickName;
    private PointLevel rank;
    private String headImage;
    private String sex;   // 0:保密,1:男,2:女
    private String realName;
    private String mobile;
    private String idCard;  // 身份证号
    private String address;
    private String isMobileVerify;  // 是否验证过手机  1:是,0:否
    private int attention;  // 关注度
    private int fans;  // 粉丝数
    private String type;  // 类型  1:普通
    private Area province;
    private Area city;
    private Area county;
    private int age;
    private String constellation;  // 星座
    private String occupation;  // 职业
    private List<String> hobbyList;  // 爱好列表


    @Override
    public String toString() {
        return "Member{" +
                "userName='" + userName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", rank=" + rank +
                ", headImage='" + headImage + '\'' +
                ", sex='" + sex + '\'' +
                ", realName='" + realName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", idCard='" + idCard + '\'' +
                ", address='" + address + '\'' +
                ", isMobileVerify='" + isMobileVerify + '\'' +
                ", attention=" + attention +
                ", fans=" + fans +
                ", type='" + type + '\'' +
                ", province=" + province +
                ", city=" + city +
                ", county=" + county +
                ", age=" + age +
                ", constellation='" + constellation + '\'' +
                ", occupation='" + occupation + '\'' +
                ", hobbyList=" + hobbyList +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public PointLevel getRank() {
        return rank;
    }

    public void setRank(PointLevel rank) {
        this.rank = rank;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIsMobileVerify() {
        return isMobileVerify;
    }

    public void setIsMobileVerify(String isMobileVerify) {
        this.isMobileVerify = isMobileVerify;
    }

    public int getAttention() {
        return attention;
    }

    public void setAttention(int attention) {
        this.attention = attention;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Area getProvince() {
        return province;
    }

    public void setProvince(Area province) {
        this.province = province;
    }

    public Area getCity() {
        return city;
    }

    public void setCity(Area city) {
        this.city = city;
    }

    public Area getCounty() {
        return county;
    }

    public void setCounty(Area county) {
        this.county = county;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public List<String> getHobbyList() {
        return hobbyList;
    }

    public void setHobbyList(List<String> hobbyList) {
        this.hobbyList = hobbyList;
    }

}
