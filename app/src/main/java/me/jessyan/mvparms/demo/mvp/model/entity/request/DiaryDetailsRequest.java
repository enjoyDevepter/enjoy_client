package me.jessyan.mvparms.demo.mvp.model.entity.request;

/**
 * Created by guomin on 2018/7/25.
 */

public class DiaryDetailsRequest extends BaseRequest {
    private int cmd;
    private String token;
    private String diaryId;


    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

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

    @Override
    public String toString() {
        return "DiaryDetailsRequest{" +
                "cmd=" + cmd +
                ", token='" + token + '\'' +
                ", diaryId='" + diaryId + '\'' +
                '}';
    }
}

