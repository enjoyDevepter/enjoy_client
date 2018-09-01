package me.jessyan.mvparms.demo.mvp.model.entity.user.bean;

import java.io.Serializable;

/**提现记录*/
public class DrawCashBean implements Serializable {
    private String bankName;
    private String cardNo;
    private String status;
    private String statusDesc;
    private String image;
    private long money;
    private String createDate;

    @Override
    public String toString() {
        return "DrawCashBean{" +
                "bankName='" + bankName + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", status='" + status + '\'' +
                ", statusDesc='" + statusDesc + '\'' +
                ", image='" + image + '\'' +
                ", money=" + money +
                ", createDate='" + createDate + '\'' +
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
