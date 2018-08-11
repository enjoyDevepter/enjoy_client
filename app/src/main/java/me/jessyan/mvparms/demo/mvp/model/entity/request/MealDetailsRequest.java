package me.jessyan.mvparms.demo.mvp.model.entity.request;

/**
 * Created by guomin on 2018/7/29.
 */

public class MealDetailsRequest extends BaseRequest {
    private int cmd;
    private String setMealId;
    private String token;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getSetMealId() {
        return setMealId;
    }

    public void setSetMealId(String setMealId) {
        this.setMealId = setMealId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "MealDetailsRequest{" +
                "cmd=" + cmd +
                ", setMealId='" + setMealId + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
