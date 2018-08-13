package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.DiaryNavi;

/**
 * Created by guomin on 2018/7/28.
 */

public class DiaryTypeListResponse extends BaseResponse {
    private List<DiaryNavi> diaryNavList;

    public List<DiaryNavi> getDiaryNavList() {
        return diaryNavList;
    }

    public void setDiaryNavList(List<DiaryNavi> diaryNavList) {
        this.diaryNavList = diaryNavList;
    }

    @Override
    public String toString() {
        return "DiaryTypeListResponse{" +
                "diaryNavList=" + diaryNavList +
                '}';
    }
}
