package me.jessyan.mvparms.demo.mvp.model.entity.user.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.CommonUserInfo;

public class CommonUserInfoResponse extends BaseResponse {
    private List<CommonUserInfo> dictList;

    public List<CommonUserInfo> getDictList() {
        return dictList;
    }

    public void setDictList(List<CommonUserInfo> dictList) {
        this.dictList = dictList;
    }

    @Override
    public String toString() {
        return "CommonUserInfoResponse{" +
                "dictList=" + dictList +
                '}';
    }
}
