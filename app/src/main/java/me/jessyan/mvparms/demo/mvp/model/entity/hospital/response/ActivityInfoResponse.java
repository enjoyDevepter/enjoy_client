package me.jessyan.mvparms.demo.mvp.model.entity.hospital.response;

import me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean.ActivityInfo;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;

public class ActivityInfoResponse extends BaseResponse {
    private ActivityInfo activityInfo;

    public ActivityInfo getActivityInfo() {
        return activityInfo;
    }

    public void setActivityInfo(ActivityInfo activityInfo) {
        this.activityInfo = activityInfo;
    }
}
