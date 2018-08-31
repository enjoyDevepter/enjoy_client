package me.jessyan.mvparms.demo.mvp.model.entity.user.request;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

public class SetCashPasswordRequest extends BaseRequest {
    private final int cmd = 1113;
    private String payPwd;  // 新密码
    private String confirmPayPwd;
    private String verifyCode;  // 短信验证码
    private String token;

    @Override
    public String toString() {
        return "SetCashPasswordRequest{" +
                "cmd=" + cmd +
                ", payPwd='" + payPwd + '\'' +
                ", confirmPayPwd='" + confirmPayPwd + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    public int getCmd() {
        return cmd;
    }

    public String getPayPwd() {
        return payPwd;
    }

    public void setPayPwd(String payPwd) {
        this.payPwd = payPwd;
    }

    public String getConfirmPayPwd() {
        return confirmPayPwd;
    }

    public void setConfirmPayPwd(String confirmPayPwd) {
        this.confirmPayPwd = confirmPayPwd;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
