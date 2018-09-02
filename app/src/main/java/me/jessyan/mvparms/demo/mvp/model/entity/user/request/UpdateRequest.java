package me.jessyan.mvparms.demo.mvp.model.entity.user.request;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

public class UpdateRequest extends BaseRequest {
    private final int cmd = 911;
    private String type;

    public int getCmd() {
        return cmd;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "UpdateRequest{" +
                "cmd=" + cmd +
                ", type='" + type + '\'' +
                '}';
    }
}
