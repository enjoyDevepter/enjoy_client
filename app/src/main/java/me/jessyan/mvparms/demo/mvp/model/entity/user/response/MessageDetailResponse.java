package me.jessyan.mvparms.demo.mvp.model.entity.user.response;

import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.DynamicMessage;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.NoticeMessage;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.PrivateMessage;

public class MessageDetailResponse extends BaseResponse {

    private PrivateMessage privateMessage;
    private NoticeMessage notice;
    private DynamicMessage dynamic;

    public NoticeMessage getNotice() {
        return notice;
    }

    public void setNotice(NoticeMessage notice) {
        this.notice = notice;
    }

    public PrivateMessage getPrivateMessage() {
        return privateMessage;
    }

    public void setPrivateMessage(PrivateMessage privateMessage) {
        this.privateMessage = privateMessage;
    }

    public DynamicMessage getDynamic() {
        return dynamic;
    }

    public void setDynamic(DynamicMessage dynamic) {
        this.dynamic = dynamic;
    }

    @Override
    public String toString() {
        return "MessageDetailResponse{" +
                "privateMessage=" + privateMessage +
                ", notice=" + notice +
                ", dynamic=" + dynamic +
                '}';
    }
}
