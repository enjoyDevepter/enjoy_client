package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.DiaryComment;

/**
 * Created by guomin on 2018/7/28.
 */

public class DiaryCommentListResponse extends BaseResponse {
    private List<DiaryComment> diaryCommentList;
    private int nextPageIndex;

    public List<DiaryComment> getDiaryCommentList() {
        return diaryCommentList;
    }

    public void setDiaryCommentList(List<DiaryComment> diaryCommentList) {
        this.diaryCommentList = diaryCommentList;
    }

    public int getNextPageIndex() {
        return nextPageIndex;
    }

    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }

    @Override
    public String toString() {
        return "DiaryCommentListResponse{" +
                "diaryCommentList=" + diaryCommentList +
                ", nextPageIndex=" + nextPageIndex +
                '}';
    }
}
