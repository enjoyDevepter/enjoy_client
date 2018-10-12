package me.jessyan.mvparms.demo.mvp.model.entity.hospital.request;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

public class ActivityInfoRequest extends BaseRequest {
    private final int cmd = 924;
    private String activityId;

    public int getCmd() {
        return cmd;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
}

