package me.jessyan.mvparms.demo.mvp.model.entity.response;

import me.jessyan.mvparms.demo.mvp.model.entity.Diary;
import me.jessyan.mvparms.demo.mvp.model.entity.DiaryMember;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;

/**
 * Created by guomin on 2018/7/28.
 */

public class DiaryDetailsResponse extends BaseResponse {

    private Diary diary;
    private DiaryMember member;
    private Goods goods;

    public Diary getDiary() {
        return diary;
    }

    public void setDiary(Diary diary) {
        this.diary = diary;
    }

    public DiaryMember getMember() {
        return member;
    }

    public void setMember(DiaryMember member) {
        this.member = member;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    @Override
    public String toString() {
        return "DiaryDetailsResponse{" +
                "diary=" + diary +
                ", member=" + member +
                ", goods=" + goods +
                '}';
    }
}
