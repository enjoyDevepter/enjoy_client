package me.jessyan.mvparms.demo.mvp.model.entity.response;

/**
 * Created by guomin on 2018/7/25.
 */

public class RegisterResponse extends BaseResponse {

    private String token;
    private String signkey;
    private String url;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "RegisterResponse{" +
                "token='" + token + '\'' +
                ", signkey='" + signkey + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
