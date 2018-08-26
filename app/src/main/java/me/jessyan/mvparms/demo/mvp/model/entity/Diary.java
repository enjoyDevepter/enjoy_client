package me.jessyan.mvparms.demo.mvp.model.entity;


import java.util.List;

/**
 * 日志
 */
public class Diary {
    private int praise;
    private String isPraise;
    private String publishDate;
    private int comment;
    private String diaryId;
    private List<String> imageList;
    private String title;
    private Member member;
    private Goods goods;
    private int browse;
    private String intro;

    public int getBrowse() {
        return browse;
    }

    public void setBrowse(int browse) {
        this.browse = browse;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getPraise() {
        return praise;
    }

    public void setPraise(int praise) {
        this.praise = praise;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getDiaryId() {
        return diaryId;
    }

    public void setDiaryId(String diaryId) {
        this.diaryId = diaryId;
    }

    public String getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(String isPraise) {
        this.isPraise = isPraise;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Diary{" +
                "browse=" + browse +
                ", comment=" + comment +
                ", praise=" + praise +
                ", publishDate='" + publishDate + '\'' +
                ", intro='" + intro + '\'' +
                ", diaryId='" + diaryId + '\'' +
                ", isPraise='" + isPraise + '\'' +
                ", title='" + title + '\'' +
                ", imageList=" + imageList +
                ", member=" + member +
                ", goods=" + goods +
                '}';
    }
}
