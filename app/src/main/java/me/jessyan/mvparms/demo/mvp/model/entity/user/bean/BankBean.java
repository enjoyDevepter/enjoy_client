package me.jessyan.mvparms.demo.mvp.model.entity.user.bean;

import java.io.Serializable;

public class BankBean implements Serializable {
    private String bankId;
    private String name;
    private String image;

    @Override
    public String toString() {
        return "BankBean{" +
                "bankId='" + bankId + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
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
