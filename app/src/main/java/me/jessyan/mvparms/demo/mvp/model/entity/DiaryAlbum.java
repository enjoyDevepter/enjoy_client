package me.jessyan.mvparms.demo.mvp.model.entity;


/**
 * 日志
 */
public class DiaryAlbum {
    private String image;
    private long createDate;
    private String diaryAlbumId;
    private int num;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getDiaryAlbumId() {
        return diaryAlbumId;
    }

    public void setDiaryAlbumId(String diaryAlbumId) {
        this.diaryAlbumId = diaryAlbumId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "DiaryAlbum{" +
                "image='" + image + '\'' +
                ", createDate=" + createDate +
                ", diaryAlbumId='" + diaryAlbumId + '\'' +
                ", num=" + num +
                '}';
    }
}
