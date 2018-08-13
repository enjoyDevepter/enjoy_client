package me.jessyan.mvparms.demo.mvp.model.entity.request;

/**
 * Created by guomin on 2018/7/25.
 */

public class DiaryVoteRequest extends BaseRequest {
    private int cmd;
    private String token;
    private String diaryId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDiaryId() {
        return diaryId;
    }

    public void setDiaryId(String diaryId) {
        this.diaryId = diaryId;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    @Override
    public String toString() {
        return "DiaryVoteRequest{" +
                "cmd=" + cmd +
                ", token='" + token + '\'' +
                ", diaryId='" + diaryId + '\'' +
                '}';
    }
}
