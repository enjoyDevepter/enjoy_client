package me.jessyan.mvparms.demo.mvp.model.entity.hospital.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean.ActivityInfo;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;

public class ActivityInfoListResponse extends BaseResponse {

    private List<ActivityInfo> activityInfoList;

    public List<ActivityInfo> getActivityInfoList() {
        return activityInfoList;
    }

    public void setActivityInfoList(List<ActivityInfo> activityInfoList) {
        this.activityInfoList = activityInfoList;
    }

    @Override
    public String toString() {
        return "ActivityInfoListResponse{" +
                "activityInfoList=" + activityInfoList +
                '}';
    }
}
