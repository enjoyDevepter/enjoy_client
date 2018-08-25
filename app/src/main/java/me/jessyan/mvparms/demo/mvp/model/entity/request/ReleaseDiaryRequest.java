package me.jessyan.mvparms.demo.mvp.model.entity.request;

import me.jessyan.mvparms.demo.mvp.model.entity.DiaryBean;

/**
 * Created by guomin on 2018/7/25.
 */

public class ReleaseDiaryRequest extends BaseRequest {
    private int cmd = 802;
    private String token;
    private DiaryBean diary;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public DiaryBean getDiary() {
        return diary;
    }

    public void setDiary(DiaryBean diary) {
        this.diary = diary;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "ReleaseDiaryRequest{" +
                "cmd=" + cmd +
                ", token='" + token + '\'' +
                ", diary=" + diary +
                '}';
    }
}
