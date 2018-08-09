package me.jessyan.mvparms.demo.mvp.model.entity.request;

import java.util.List;

/**
 * Created by guomin on 2018/7/25.
 */

public class DeleteCartListRequest extends BaseRequest {
    private int cmd = 1007;
    private String token;
    private List<GoodsBean> goodsList;

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

    public List<GoodsBean> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsBean> goodsList) {
        this.goodsList = goodsList;
    }


    @Override
    public String toString() {
        return "DeleteCartListRequest{" +
                "cmd=" + cmd +
                ", token='" + token + '\'' +
                ", goodsList=" + goodsList +
                '}';
    }

    public static class GoodsBean {
        private String goodsId;
        private String merchId;

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
            return "GoodsBean{" +
                    "goodsId='" + goodsId + '\'' +
                    ", merchId='" + merchId + '\'' +
                    '}';
        }
    }
}