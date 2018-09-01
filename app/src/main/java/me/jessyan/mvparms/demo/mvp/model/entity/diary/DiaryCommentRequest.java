package me.jessyan.mvparms.demo.mvp.model.entity.diary;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

/**
 * Created by guomin on 2018/8/28.
 */

public class DiaryCommentRequest extends BaseRequest {
    private int cmd = 810;
    private String diaryId;
    private String token;
    private String content;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getDiaryId() {
        return diaryId;
    }

    public void setDiaryId(String diaryId) {
        this.diaryId = diaryId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "DiaryCommentRequest{" +
                "cmd=" + cmd +
                ", diaryId='" + diaryId + '\'' +
                ", token='" + token + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
