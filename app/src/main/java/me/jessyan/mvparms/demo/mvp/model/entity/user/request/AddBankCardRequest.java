package me.jessyan.mvparms.demo.mvp.model.entity.user.request;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.BankCardBean;

public class AddBankCardRequest extends BaseRequest {
    private final int cmd = 208;
    private String token;
    private BankCardBean bankCard;

    @Override
    public String toString() {
        return "AddBankCardRequest{" +
                "cmd=" + cmd +
                ", token='" + token + '\'' +
                ", bankCard=" + bankCard +
                '}';
    }

    public int getCmd() {
        return cmd;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public BankCardBean getBankCard() {
        return bankCard;
    }

    public void setBankCard(BankCardBean bankCard) {
        this.bankCard = bankCard;
    }
}
