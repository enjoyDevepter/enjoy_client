package me.jessyan.mvparms.demo.mvp.model.entity.request;

/**
 * Created by guomin on 2018/7/25.
 */

public class ForgetRequest extends BaseRequest {
    private String mobile;
    private String verifyCode;
    private int cmd = 108;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    @Override
    public String toString() {
        return "LoginByPhoneRequest{" +
                "mobile='" + mobile + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                ", cmd=" + cmd +
                '}';
    }
}
