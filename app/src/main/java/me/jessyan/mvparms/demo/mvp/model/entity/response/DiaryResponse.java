package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.DiaryAlbum;
import me.jessyan.mvparms.demo.mvp.model.entity.DiaryGoods;
import me.jessyan.mvparms.demo.mvp.model.entity.Member;

/**
 * Created by guomin on 2018/7/28.
 */

public class DiaryResponse extends BaseResponse {

    private Member member;
    private List<DiaryAlbum> diaryAlbumList;
    private DiaryGoods goods;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public List<DiaryAlbum> getDiaryAlbumList() {
        return diaryAlbumList;
    }

    public void setDiaryAlbumList(List<DiaryAlbum> diaryAlbumList) {
        this.diaryAlbumList = diaryAlbumList;
    }

    public DiaryGoods getGoods() {
        return goods;
    }

    public void setGoods(DiaryGoods goods) {
        this.goods = goods;
    }

    @Override
    public String toString() {
        return "DiaryResponse{" +
                "member=" + member +
                ", diaryAlbumList=" + diaryAlbumList +
                ", goods=" + goods +
                '}';
    }
}
