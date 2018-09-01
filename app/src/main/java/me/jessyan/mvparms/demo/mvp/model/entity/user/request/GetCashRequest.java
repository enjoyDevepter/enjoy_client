package me.jessyan.mvparms.demo.mvp.model.entity.user.request;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

public class GetCashRequest extends BaseRequest {
    private final int cmd = 1260;
    private String bankCardId;
    private long money;  // 提现金额，单位分
    private String payPwd;  // 支付密码
    private String token;

    @Override
    public String toString() {
        return "GetCashRequest{" +
                "cmd=" + cmd +
                ", bankCardId='" + bankCardId + '\'' +
                ", money=" + money +
                ", payPwd='" + payPwd + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    public int getCmd() {
        return cmd;
    }

    public String getBankCardId() {
        return bankCardId;
    }

    public void setBankCardId(String bankCardId) {
        this.bankCardId = bankCardId;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public String getPayPwd() {
        return payPwd;
    }

    public void setPayPwd(String payPwd) {
        this.payPwd = payPwd;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
