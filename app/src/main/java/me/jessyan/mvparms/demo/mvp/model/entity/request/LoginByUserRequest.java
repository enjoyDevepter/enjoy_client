package me.jessyan.mvparms.demo.mvp.model.entity.request;

/**
 * Created by guomin on 2018/7/25.
 */

public class LoginByUserRequest extends BaseRequest {
    private String username;
    private String password;
    private int cmd = 102;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    @Override
    public String toString() {
        return "LoginByUserRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", cmd=" + cmd +
                '}';
    }
}
