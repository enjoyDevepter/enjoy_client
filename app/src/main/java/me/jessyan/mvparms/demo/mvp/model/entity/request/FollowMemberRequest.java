package me.jessyan.mvparms.demo.mvp.model.entity.request;

/**
 * Created by guomin on 2018/7/25.
 */

public class FollowMemberRequest extends BaseRequest {
    private int cmd;
    private String token;
    private String memberId;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "FollowMemberRequest{" +
                "cmd=" + cmd +
                ", token='" + token + '\'' +
                ", memberId='" + memberId + '\'' +
                '}';
    }
}
