package me.jessyan.mvparms.demo.mvp.model.entity.user.request;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

/**现金币转消费币*/
public class CashConvertRequest extends BaseRequest {
    private final int cmd = 1250;
    private int money;  // 单位元
    private String token;

    @Override
    public String toString() {
        return "CashConvertRequest{" +
                "cmd=" + cmd +
                ", money=" + money +
                ", token='" + token + '\'' +
                '}';
    }

    public int getCmd() {
        return cmd;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
