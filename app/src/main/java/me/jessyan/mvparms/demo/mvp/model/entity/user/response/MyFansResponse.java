package me.jessyan.mvparms.demo.mvp.model.entity.user.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.FansMember;

public class MyFansResponse extends BaseResponse {
    private int nextPageIndex;
    private List<FansMember> memberList;

    @Override
    public String toString() {
        return "MyFansResponse{" +
                "nextPageIndex=" + nextPageIndex +
                ", memberList=" + memberList +
                '}';
    }

    public int getNextPageIndex() {
        return nextPageIndex;
    }

    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }

    public List<FansMember> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<FansMember> memberList) {
        this.memberList = memberList;
    }
}
