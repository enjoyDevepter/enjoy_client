package me.jessyan.mvparms.demo.mvp.model.entity.response;

/**
 * Created by guomin on 2018/7/25.
 */

public class RegisterResponse extends BaseResponse {

    private String token;
    private String signkey;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSignkey() {
        return signkey;
    }

    public void setSignkey(String signkey) {
        this.signkey = signkey;
    }
}
