package me.jessyan.mvparms.demo.mvp.model.entity.request;

/**
 * Created by guomin on 2018/7/25.
 */

public class DiaryRequest extends BaseRequest {
    private int cmd = 808;
    private String token;
    private String diaryId;
    private String goodsId;
    private String merchId;


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

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getMerchId() {
        return merchId;
    }

    public void setMerchId(String merchId) {
        this.merchId = merchId;
    }

    @Override
    public String toString() {
        return "DiaryRequest{" +
                "cmd=" + cmd +
                ", token='" + token + '\'' +
                ", diaryId='" + diaryId + '\'' +
                ", goodsId='" + goodsId + '\'' +
                ", merchId='" + merchId + '\'' +
                '}';
    }
}

