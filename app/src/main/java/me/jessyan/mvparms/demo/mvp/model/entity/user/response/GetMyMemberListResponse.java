package me.jessyan.mvparms.demo.mvp.model.entity.user.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.MyMemberBean;

public class GetMyMemberListResponse extends BaseResponse {
    private int nextPageIndex;
    private String url;
    private List<MyMemberBean> memberList;

    @Override
    public String toString() {
        return "GetMyMemberListResponse{" +
                "nextPageIndex=" + nextPageIndex +
                ", url='" + url + '\'' +
                ", memberList=" + memberList +
                '}';
    }

    public int getNextPageIndex() {
        return nextPageIndex;
    }

    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<MyMemberBean> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<MyMemberBean> memberList) {
        this.memberList = memberList;
    }
}
