package me.jessyan.mvparms.demo.mvp.model.entity.request;

/**
 * Created by guomin on 2018/7/25.
 */

public class DiaryCommentListRequest extends BaseRequest {
    private int cmd = 809;
    private int pageIndex = 1;
    private int pageSize = 10;
    private String diaryId;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getDiaryId() {
        return diaryId;
    }

    public void setDiaryId(String diaryId) {
        this.diaryId = diaryId;
    }

    @Override
    public String toString() {
        return "DiaryCommentListRequest{" +
                "cmd=" + cmd +
                ", pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                ", diaryId='" + diaryId + '\'' +
                '}';
    }
}

