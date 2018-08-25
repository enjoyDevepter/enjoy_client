package me.jessyan.mvparms.demo.mvp.model.entity.user.response;

import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.Member;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.MemberAccount;

public class UserInfoResponse extends BaseResponse {
    private MemberAccount memberAccount;  // 账户
    private Member member;

    @Override
    public String toString() {
        return "UserInfoResponse{" +
                "memberAccount=" + memberAccount +
                ", member=" + member +
                '}';
    }

    public MemberAccount getMemberAccount() {
        return memberAccount;
    }

    public void setMemberAccount(MemberAccount memberAccount) {
        this.memberAccount = memberAccount;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
