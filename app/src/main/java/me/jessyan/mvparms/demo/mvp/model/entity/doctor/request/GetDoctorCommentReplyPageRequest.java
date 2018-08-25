package me.jessyan.mvparms.demo.mvp.model.entity.doctor.request;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

public class GetDoctorCommentReplyPageRequest extends BaseRequest {
    private final int cmd = 669;
    private int pageSize;
    private int pageIndex;
    private String commentId;

    @Override
    public String toString() {
        return "GetDoctorCommentReplyPageRequest{" +
                "cmd=" + cmd +
                ", pageSize=" + pageSize +
                ", pageIndex=" + pageIndex +
                ", commentId='" + commentId + '\'' +
                '}';
    }

    public int getCmd() {
        return cmd;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }
}
