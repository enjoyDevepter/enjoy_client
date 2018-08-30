package me.jessyan.mvparms.demo.mvp.model.entity.user.bean;

import java.io.Serializable;

public class BankCardBean implements Serializable {
    private String bankName;
    private String cardNo;
    private String bankId;
    private String id;
    private String isDefaultIn;
    private String name;
    private String image;

    @Override
    public String toString() {
        return "BankCardBean{" +
                "bankName='" + bankName + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", bankId='" + bankId + '\'' +
                ", id='" + id + '\'' +
                ", isDefaultIn='" + isDefaultIn + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsDefaultIn() {
        return isDefaultIn;
    }

    public void setIsDefaultIn(String isDefaultIn) {
        this.isDefaultIn = isDefaultIn;
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
}
