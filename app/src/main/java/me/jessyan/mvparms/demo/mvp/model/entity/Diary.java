package me.jessyan.mvparms.demo.mvp.model.entity;


import java.util.List;

/**
 * 日记
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
    private String content;
    private String isShare;
    private String shareTitle;
    private String shareDesc;
    private String shareUrl;
    private String shareImageUrl;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIsShare() {
        return isShare;
    }

    public void setIsShare(String isShare) {
        this.isShare = isShare;
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

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getShareImageUrl() {
        return shareImageUrl;
    }

    public void setShareImageUrl(String shareImageUrl) {
        this.shareImageUrl = shareImageUrl;
    }

    @Override
    public String toString() {
        return "Diary{" +
                "praise=" + praise +
                ", isPraise='" + isPraise + '\'' +
                ", publishDate='" + publishDate + '\'' +
                ", comment=" + comment +
                ", diaryId='" + diaryId + '\'' +
                ", imageList=" + imageList +
                ", title='" + title + '\'' +
                ", member=" + member +
                ", goods=" + goods +
                ", browse=" + browse +
                ", intro='" + intro + '\'' +
                ", content='" + content + '\'' +
                ", isShare='" + isShare + '\'' +
                ", shareTitle='" + shareTitle + '\'' +
                ", shareDesc='" + shareDesc + '\'' +
                ", shareUrl='" + shareUrl + '\'' +
                ", shareImageUrl='" + shareImageUrl + '\'' +
                '}';
    }
}
