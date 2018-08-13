package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.Diary;

/**
 * Created by guomin on 2018/7/28.
 */

public class DiaryListResponse extends BaseResponse {
    private List<Diary> diaryList;
    private int nextPageIndex;

    public List<Diary> getDiaryList() {
        return diaryList;
    }

    public void setDiaryList(List<Diary> diaryList) {
        this.diaryList = diaryList;
    }

    public int getNextPageIndex() {
        return nextPageIndex;
    }

    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }

    @Override
    public String toString() {
        return "DiaryListResponse{" +
                "diaryList=" + diaryList +
                ", nextPageIndex=" + nextPageIndex +
                '}';
    }
}
