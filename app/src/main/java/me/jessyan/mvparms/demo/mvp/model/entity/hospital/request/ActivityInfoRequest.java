package me.jessyan.mvparms.demo.mvp.model.entity.hospital.request;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

public class ActivityInfoRequest extends BaseRequest {
    private int cmd = 924;
    private String activityId;
    private String hospitalId;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    @Override
    public String toString() {
        return "ActivityInfoRequest{" +
                "cmd=" + cmd +
                ", activityId='" + activityId + '\'' +
                ", hospitalId='" + hospitalId + '\'' +
                '}';
    }
}

