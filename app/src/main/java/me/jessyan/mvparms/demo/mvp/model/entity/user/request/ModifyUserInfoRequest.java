package me.jessyan.mvparms.demo.mvp.model.entity.user.request;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

public class ModifyUserInfoRequest extends BaseRequest {
    private int cmd;
    private String token;
    private String headImage;
    private String nickName;
    private String realName;
    private String sex;
    private String province;
    private String city;
    private String county;
    private String constellation;
    private int age;
    private String hobby;
    private String occupation;
    private String oldUserPwd;
    private String newUserPwd;
    private String confirmUserPwd;
    private String password;
    private String confirmPassword;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getOldUserPwd() {
        return oldUserPwd;
    }

    public void setOldUserPwd(String oldUserPwd) {
        this.oldUserPwd = oldUserPwd;
    }

    public String getNewUserPwd() {
        return newUserPwd;
    }

    public void setNewUserPwd(String newUserPwd) {
        this.newUserPwd = newUserPwd;
    }

    public String getConfirmUserPwd() {
        return confirmUserPwd;
    }

    public void setConfirmUserPwd(String confirmUserPwd) {
        this.confirmUserPwd = confirmUserPwd;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override
    public String toString() {
        return "ModifyUserInfoRequest{" +
                "cmd=" + cmd +
                ", token='" + token + '\'' +
                ", headImage='" + headImage + '\'' +
                ", nickName='" + nickName + '\'' +
                ", realName='" + realName + '\'' +
                ", sex='" + sex + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", county='" + county + '\'' +
                ", constellation='" + constellation + '\'' +
                ", age=" + age +
                ", hobby='" + hobby + '\'' +
                ", occupation='" + occupation + '\'' +
                ", oldUserPwd='" + oldUserPwd + '\'' +
                ", newUserPwd='" + newUserPwd + '\'' +
                ", confirmUserPwd='" + confirmUserPwd + '\'' +
                '}';
    }
}
