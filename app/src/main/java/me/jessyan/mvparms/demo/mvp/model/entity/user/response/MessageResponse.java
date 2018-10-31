package me.jessyan.mvparms.demo.mvp.model.entity.user.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.CommentMessage;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.DynamicMessage;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.NoticeMessage;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.PraiseMessage;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.PrivateMessage;

public class MessageResponse extends BaseResponse {
    private int nextPageIndex;
    private List<PrivateMessage> privateMessageList;
    private List<NoticeMessage> noticeList;
    private List<DynamicMessage> dynamicList;
    private List<CommentMessage> diaryCommentList;
    private List<PraiseMessage> diaryPraiseList;

    public int getNextPageIndex() {
        return nextPageIndex;
    }

    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }

    public List<PrivateMessage> getPrivateMessageList() {
        return privateMessageList;
    }

    public void setPrivateMessageList(List<PrivateMessage> privateMessageList) {
        this.privateMessageList = privateMessageList;
    }

    public List<NoticeMessage> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<NoticeMessage> noticeList) {
        this.noticeList = noticeList;
    }

    public List<DynamicMessage> getDynamicList() {
        return dynamicList;
    }

    public void setDynamicList(List<DynamicMessage> dynamicList) {
        this.dynamicList = dynamicList;
    }

    public List<CommentMessage> getDiaryCommentList() {
        return diaryCommentList;
    }

    public void setDiaryCommentList(List<CommentMessage> diaryCommentList) {
        this.diaryCommentList = diaryCommentList;
    }

    public List<PraiseMessage> getDiaryPraiseList() {
        return diaryPraiseList;
    }

    public void setDiaryPraiseList(List<PraiseMessage> diaryPraiseList) {
        this.diaryPraiseList = diaryPraiseList;
    }

    @Override
    public String toString() {
        return "MessageResponse{" +
                "nextPageIndex=" + nextPageIndex +
                ", privateMessageList=" + privateMessageList +
                ", noticeList=" + noticeList +
                ", dynamicList=" + dynamicList +
                ", diaryCommentList=" + diaryCommentList +
                ", diaryPraiseList=" + diaryPraiseList +
                '}';
    }
}
