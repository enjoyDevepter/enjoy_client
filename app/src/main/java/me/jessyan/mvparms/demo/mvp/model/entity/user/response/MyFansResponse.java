package me.jessyan.mvparms.demo.mvp.model.entity.user.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.Member;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;

public class MyFansResponse extends BaseResponse {
    private int nextPageIndex;
    private List<Member> memberList;

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

    public List<Member> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<Member> memberList) {
        this.memberList = memberList;
    }
}
