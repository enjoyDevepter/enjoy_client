package me.jessyan.mvparms.demo.mvp.model.entity.response;

import me.jessyan.mvparms.demo.mvp.model.entity.Diary;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.model.entity.Member;

/**
 * Created by guomin on 2018/7/28.
 */

public class DiaryDetailsResponse extends BaseResponse {

    private Diary diary;
    private Member member;
    private Goods goods;
    private String shareUrl;
    private String shareTitle;
    private String shareDesc;

    public Diary getDiary() {
        return diary;
    }

    public void setDiary(Diary diary) {
        this.diary = diary;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareDesc() {
        return shareDesc;
    }

    public void setShareDesc(String shareDesc) {
        this.shareDesc = shareDesc;
    }

    @Override
    public String toString() {
        return "DiaryDetailsResponse{" +
                "diary=" + diary +
                ", member=" + member +
                ", goods=" + goods +
                ", shareUrl='" + shareUrl + '\'' +
                ", shareTitle='" + shareTitle + '\'' +
                ", shareDesc='" + shareDesc + '\'' +
                '}';
    }
}
