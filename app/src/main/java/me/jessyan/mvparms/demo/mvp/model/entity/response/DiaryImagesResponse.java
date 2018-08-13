package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.DiaryAlbum;

/**
 * Created by guomin on 2018/7/28.
 */

public class DiaryImagesResponse extends BaseResponse {

    private List<DiaryAlbum> diaryAlbumList;
    private int nextPageIndex;

    public List<DiaryAlbum> getDiaryAlbumList() {
        return diaryAlbumList;
    }

    public void setDiaryAlbumList(List<DiaryAlbum> diaryAlbumList) {
        this.diaryAlbumList = diaryAlbumList;
    }

    public int getNextPageIndex() {
        return nextPageIndex;
    }

    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }

    @Override
    public String toString() {
        return "DiaryImagesResponse{" +
                "diaryAlbumList=" + diaryAlbumList +
                ", nextPageIndex=" + nextPageIndex +
                '}';
    }
}
